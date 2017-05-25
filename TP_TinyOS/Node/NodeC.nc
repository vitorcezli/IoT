#include "Timer.h"
#include "nodes_comm.h"

/**
 *
 * @author Rafagan Soares
 * @date   15 May 2017
 */

 module NodeC @safe(){

    uses
    {
        /* General use. */
        interface Boot;
        interface Leds;
        interface Timer<TMilli> as MilliTimerTop;
        interface Timer<TMilli> as MilliTimerDad;

        /* Sensores */
        interface Read <uint16_t> as TSensor;
        interface Read <uint16_t> as PSensor;

        /* Comunicacao de Radio */
        interface Packet;
        interface AMPacket;
        interface AMSend as AMSendReqTop;
        interface AMSend as AMSendRespTop;
        interface AMSend as AMSendReqDado;
        interface AMSend as AMSendRespDado;
        interface SplitControl as AMControl;
        interface Receive as ReceiveReqTop;
        interface Receive as ReceiveRespTop;
        interface Receive as ReceiveReqDado;
        interface Receive as ReceiveRespDado;
    }

}


implementation{
    /* Radio Variables */
    bool _isBusy = FALSE;
    message_t _packet;
    
    /* Variaveis de sistema RSSF */
    bool _TDataOK = FALSE, _PDataOK = FALSE;
    bool _isReadingSensorT = FALSE;
    bool _isReadingSensorP = FALSE;
    nx_uint8_t meuPai;
    nx_uint16_t idReqLocal;
    bool visitado=FALSE;
    reqTopMsg_t reqtop;
    respTopMsg_t resptop;
    reqDadoMsg_t reqdado;
    respDadoMsg_t respdado;


    /* Procedimento de recebimento de mensagens. 
     * receber -> verificar se é resposta 
     * se não for resposta, mandar para os outros ler a temperatura e a luminosidade
     * responder 
     */
     
	void report_problem() { call Leds.led0Toggle(); }
	void report_sent() { call Leds.led1Toggle(); }
	void report_received() { call Leds.led2Toggle(); }
     
    event void Boot.booted() {
        idReqLocal = 0;
        call AMControl.start();
    }

    event void AMControl.startDone(error_t err) {
        if (err == SUCCESS) {
            //call MilliTimer.startPeriodic(TIMER_PERIOD_MILLI);
        }
        else {
            call AMControl.start();
        }
    }

    event void AMControl.stopDone(error_t err) {}

    event void AMSendReqTop.sendDone(message_t* msg, error_t error) {
        if (error == SUCCESS)
      		report_sent();
    	else
      		report_problem();
        
        if (& _packet == msg) {
            _isBusy = FALSE;
        }
    }

	event void AMSendRespTop.sendDone(message_t* msg, error_t error) {
        if (error == SUCCESS)
      		report_sent();
    	else
      		report_problem();
        
        if (& _packet == msg) {
            _isBusy = FALSE;
        }
    }

	event void AMSendReqDado.sendDone(message_t* msg, error_t error) {
        if (error == SUCCESS)
      		report_sent();
    	else
      		report_problem();
        
        if (& _packet == msg) {
            _isBusy = FALSE;
        }
    }

	event void AMSendRespDado.sendDone(message_t* msg, error_t error) {
        if (error == SUCCESS)
      		report_sent();
    	else
      		report_problem();
        
        if (& _packet == msg) {
            _isBusy = FALSE;
        }
    }

    event message_t* ReceiveReqTop.receive(message_t *msg, void *payload, uint8_t len)
    {
        if(len==sizeof(reqTopMsg_t) && !_isBusy)
        {
            reqTopMsg_t *m=(reqTopMsg_t *)payload;
            if(m->tipo==AM_REQ_TOPOLOGIA && visitado==FALSE)
            {
                //marcando
                visitado=TRUE;
                meuPai=m->src;

                //respondendo
                resptop.tipo=AM_RESP_TOPOLOGIA;
                resptop.src=(nx_uint8_t)TOS_NODE_ID;
                resptop.dst=meuPai;
                resptop.origem=meuPai;
                resptop.destino=(nx_uint8_t)TOS_NODE_ID;
                resptop.id_req=m->id_req;
                if(call AMSendRespTop.send(AM_BROADCAST_ADDR, &_packet, sizeof(respTopMsg_t))==SUCCESS)
                {
                    _isBusy=TRUE;
                }

                //espalhando
                reqtop.tipo=AM_REQ_TOPOLOGIA;
                reqtop.src=(nx_uint8_t)TOS_NODE_ID;
                reqtop.id_req=m->id_req;
                call MilliTimerTop.startOneShot(250);
            }
        }

        return msg;
    }

    event message_t* ReceiveRespTop.receive(message_t *msg, void *payload, uint8_t len)
    {
        if(len==sizeof(respTopMsg_t) && !_isBusy)
        {
            respTopMsg_t *m=(respTopMsg_t *)payload;
            if(m->tipo==AM_RESP_TOPOLOGIA && m->dst==(nx_uint8_t)TOS_NODE_ID)
            {
                //encaminhando
                resptop.tipo=AM_RESP_TOPOLOGIA;
                resptop.src=(nx_uint8_t)TOS_NODE_ID;
                resptop.dst=meuPai;
                resptop.origem=m->origem;
                resptop.destino=m->destino;
                resptop.id_req=m->id_req;
                if(call AMSendRespTop.send(AM_BROADCAST_ADDR, &_packet, sizeof(respTopMsg_t))==SUCCESS)
                {
                    _isBusy=TRUE;
                }
            }
        }

        return msg;
    }

    event message_t* ReceiveReqDado.receive(message_t *msg, void *payload, uint8_t len)
    {
        if(len==sizeof(reqDadoMsg_t) && !_isBusy)
        {
            reqDadoMsg_t *m=(reqDadoMsg_t *)payload;
            if(m->tipo==AM_REQ_DADOS && m->src==meuPai)
            {
                //respondendo
                respdado.tipo=AM_RESP_DADOS;
                respdado.src=(nx_uint8_t)TOS_NODE_ID;
                respdado.dst=meuPai;
                respdado.origem=(nx_uint8_t)TOS_NODE_ID;
                respdado.id_req=m->id_req;
                if (call TSensor.read() != SUCCESS)
                        report_problem();
                else _isReadingSensorT = TRUE;
                if (call PSensor.read() != SUCCESS)
                        report_problem();
                else _isReadingSensorP = TRUE;

                reqdado.tipo=AM_REQ_DADOS;
                reqdado.src=(nx_uint8_t)TOS_NODE_ID;
                reqdado.id_req=m->id_req;
                call MilliTimerDad.startOneShot(250);
            }
        }

        return msg;
    }

    event message_t* ReceiveRespDado.receive(message_t *msg, void *payload, uint8_t len)
    {
        if(len==sizeof(respDadoMsg_t) && !_isBusy)
        {
            respDadoMsg_t *m=(respDadoMsg_t *)payload;
            if(m->tipo==AM_RESP_DADOS && m->dst==(nx_uint8_t)TOS_NODE_ID)
            {
                respdado.tipo=AM_RESP_DADOS;
                respdado.src=(nx_uint8_t)TOS_NODE_ID;
                respdado.dst=meuPai;
                respdado.id_req=m->id_req;
                respdado.origem=m->origem;
                respdado.lum=m->lum;
                respdado.temp=m->temp;
                if(call AMSendRespDado.send(AM_BROADCAST_ADDR, &_packet, sizeof(respDadoMsg_t))==SUCCESS)
                {
                    _isBusy=TRUE;
                }
            }
        }

        return msg;
    }
	
	void checkData()
	{
		if(_TDataOK && _PDataOK){
			if(call AMSendRespDado.send(AM_BROADCAST_ADDR, &_packet, sizeof(respDadoMsg_t)) == SUCCESS)
        		_isBusy = TRUE;
        	_PDataOK = FALSE;
        	_TDataOK = FALSE;
        }
	}

	event void TSensor.readDone(error_t result, uint16_t data)
	{
		if (result != SUCCESS)
      	{
			data = 0xffff;
			report_problem();
      	}
    	
    	respdado.temp = data;
    	_TDataOK = TRUE;
        checkData();
        
       	_isReadingSensorT = FALSE;
    	
	}

	event void PSensor.readDone(error_t result, uint16_t data)
	{
		if (result != SUCCESS)
      	{
			data = 0xffff;
			report_problem();
      	}
    	
    	respdado.lum = data;
    	_PDataOK = TRUE;
        checkData();
        
       	_isReadingSensorP = FALSE;
    	
	}

	event void MilliTimerTop.fired(){
       	if(!_isBusy)
        	if(call AMSendReqTop.send(AM_BROADCAST_ADDR, &_packet, sizeof(reqTopMsg_t)) == SUCCESS)
            	_isBusy = TRUE;
	}

	event void MilliTimerDad.fired(){
        if(!_isBusy)
        	if(call AMSendReqDado.send(AM_BROADCAST_ADDR, &_packet, sizeof(reqDadoMsg_t)) == SUCCESS)
            	_isBusy = TRUE;
	}
}
