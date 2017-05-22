
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
        interface Timer<TMilli> as MilliTimer;

        /* Sensores */
        interface Read<uint16_t> as TempSensor;

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
    bool busy=FALSE;
    message_t _packet;
    
    /* Variaveis de sistema RSSF */
    nx_uint8_t meuPai;
    bool visitado=FALSE;
    msg_t m;

    /* Procedimento de recebimento de mensagens. 
     * receber -> verificar se é resposta 
     * se não for resposta, mandar para os outros ler a temperatura e a luminosidade
     * responder 
     */
    event void Boot.booted() {
        call AMControl.start();
    }

    event void AMControl.startDone(error_t err) {
        if (err == SUCCESS) {
            call MilliTimer.startPeriodic(TIMER_PERIOD_MILLI);
        }
        else {
            call AMControl.start();
        }
    }

    event void AMControl.stopDone(error_t err) {}

    event void AMSend.sendDone(message_t* msg, error_t err) {
        if (& _packet == msg) {
            busy = FALSE;
        }
    }

    event message_t* Receive.receive(message_t *msg, void *payload, uint8_t len)
    {
        if(len == sizeof(msg_t) && !busy)
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
                        busy=TRUE;
                    //espalhando
                    m.tipo=0x01;
                    if(!busy)
                        if(call AMSend.send(AM_BROADCAST_ADDR, & _packet , sizeof(msg_t)) == SUCCESS)
                            busy=TRUE;
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
                        busy=TRUE;
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
                    m.temp;
                    m.lum;
                    m.id_req=mr->id_req;
                    if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
                        busy=TRUE;

                    //espalhando
                    m.tipo=0x03;
                    if(!busy)
                        if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
                            busy=TRUE;
                }
            }
            else //resp dados
            {
                if(mr->dst==TOS_NODE_ID)
                {
                    //encaminhando
                    m.tipo=0x04;
                    m.src= (nx_uint8_t)TOS_NODE_ID;
                    m.dst=meuPai;
                    m.origem=mr->origem;
                    m.temp=mr->temp;
                    m.lum=mr->lum;
                    m.id_req=mr->id_req;
                    if(call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS)
                        busy=TRUE;
                }
            }
        }
        return msg;
    }

	event void MilliTimer.fired(){
		// TODO Auto-generated method stub
	}

	event void TempSensor.readDone(error_t result, uint16_t val){
		// TODO Auto-generated method stub
	}
}
