
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
        interface AMSend;
        interface SplitControl as AMControl;
        interface Receive;
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
    msg_t m;

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

    event void AMSend.sendDone(message_t* msg, error_t error) {
        if (error == SUCCESS)
      		report_sent();
    	else
      		report_problem();
        
        if (& _packet == msg) {
            _isBusy = FALSE;
        }
    }

    event message_t* Receive.receive(message_t *msg, void *payload, uint8_t len)
    {
        report_received();
        if(len == sizeof(msg_t) && !_isBusy)
        {
            msg_t *mr = (msg_t*) payload;
            if(mr->tipo==0x01) //req top
            {
                if(visitado==FALSE)
                {
                    //respondendo
                    visitado=TRUE;
                    meuPai=mr->src;
                    m.tipo=0x02;
                    m.src= (nx_uint8_t)TOS_NODE_ID;
                    m.dst=meuPai;
                    m.origem=meuPai;
                    m.destino=TOS_NODE_ID;
                    m.id_req=mr->id_req;
                    if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
                        _isBusy = TRUE;
                    //espalhando
                    call MilliTimerTop.startOneShot(250);
                }
            }
            else if(mr->tipo==0x02) //resp top
            {
                if(mr->dst==TOS_NODE_ID)
                {
                    //encaminhando
                    m.tipo=0x02;
                    m.src= (nx_uint8_t)TOS_NODE_ID;
                    m.dst=meuPai;
                    m.origem=mr->origem;
                    m.destino=mr->destino;
                    m.id_req=mr->id_req;
                    if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
                        _isBusy = TRUE;
                }
            }
            else if(mr->tipo==0x03) //req dados
            {
                if(mr->src==meuPai)
                {
                    //respondendo
                    m.tipo=0x04;
                    m.src= (nx_uint8_t)TOS_NODE_ID;
                    m.dst=meuPai;
                    m.origem=TOS_NODE_ID;
                    m.id_req=mr->id_req;
                    if (call TSensor.read() != SUCCESS)
      					report_problem();
      				else _isReadingSensorT = TRUE;
      				if (call PSensor.read() != SUCCESS)
      					report_problem();
      				else _isReadingSensorP = TRUE;

					call MilliTimerDad.startOneShot(250);

                    //espalhando
                    
                }
            }
            else //resp dados
            {
                if(mr->dst == TOS_NODE_ID && !_isReadingSensorT && !_isReadingSensorP)
                {
                    //encaminhando
                    m.tipo=0x04;
                    m.src= (nx_uint8_t)TOS_NODE_ID;
                    m.dst=meuPai;
                    m.origem=mr->origem;
                    m.id_req=mr->id_req;
                    if (call TSensor.read() != SUCCESS)
      					report_problem();
      				else _isReadingSensorT = TRUE;
      				if (call PSensor.read() != SUCCESS)
      					report_problem();
      				else _isReadingSensorP = TRUE;
                }
            }
        }
        if(_isBusy) report_problem();
        
        return msg;
    }
	
	void checkData()
	{
		if(_TDataOK && _PDataOK){
			if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
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
    	
    	m.temp = data;
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
    	
    	m.lum = data;
    	_PDataOK = TRUE;
        checkData();
        
       	_isReadingSensorP = FALSE;
    	
	}

	event void MilliTimerTop.fired(){
		
		
		m.tipo=0x01;
       	if(!_isBusy)
        	if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
            	_isBusy = TRUE;
	}

	event void MilliTimerDad.fired(){
		m.tipo=0x01;
        if(!_isBusy)
        	if(call AMSend.send(AM_BROADCAST_ADDR, & _packet , sizeof(msg_t)) == SUCCESS)
            	_isBusy = TRUE;
	}
}
