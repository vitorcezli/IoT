#include "Timer.h"
#include "nodes_comm.h"
#include "printf.h"

module BaseNodeC @safe(){

    uses
    {
        /* General use. */
        interface Boot;
        interface Leds;
        interface Timer<TMilli> as Timer0;
		interface Timer<TMilli> as Timer1;

        /* Comunicacao de Radio */
        interface Packet;
        interface AMPacket;
        interface AMSend;
        interface SplitControl as AMControl;
        interface Receive;
    }

}


implementation{
    /* Variaveis de comunicaçao*/
    message_t _packet;
    bool _isBusy=FALSE;
    
    /* Variaveis de comunicação RSSF */
    nx_uint16_t id_req_atual;
    reqTopMsg_t reqtop;
    reqDadoMsg_t reqdado;
    
    /* Program Function for User Control  */
    void report_problem() { call Leds.led0Toggle(); }
	void report_sent() { call Leds.led1Toggle(); }
	void report_received() { call Leds.led2Toggle(); }
    
    
    event void Boot.booted() {
      call AMControl.start();
      id_req_atual = 0;
    }

    event void AMControl.startDone(error_t err) {
        if (err == SUCCESS) {
            call Timer0.startPeriodic(1000);
            call Timer1.startPeriodic(1000);
        }
    }
    event void AMControl.stopDone(error_t err) {}

    event void Timer0.fired() {
        id_req_atual++;
        if (!_isBusy) {
            reqTopMsg_t* m=(reqTopMsg_t*)(call Packet.getPayload(&_packet, sizeof(msg_t)));
            if (m == NULL) {
                return;
            }
            m->src = (nx_uint8_t)TOS_NODE_ID;
            m->tipo = AM_REQ_TOPOLOGIA;
            m->id_req = id_req_atual;
            if (call AMSend.send(AM_BROADCAST_ADDR, m, sizeof(reqTopMsg_t)) == SUCCESS) {
                _isBusy = TRUE;
            }
        }
    }

    event void Timer1.fired() {
        id_req_atual++;
        if (!_isBusy) {
            reqDadoMsg_t* m = (msg_t*)(call Packet.getPayload(& _packet, sizeof(msg_t)));
            if (mr == NULL) {
                return;
            }
            m->src = (nx_uint8_t)TOS_NODE_ID;
            m->tipo=AM_REQ_DADOS;
            m->id_req=id_req_atual;
            if (call AMSend.send(AM_BROADCAST_ADDR, m, sizeof(reqDadoMsg_t)) == SUCCESS) {
                _isBusy = TRUE;
            }
        }
    }

    event message_t* Receive.receive(message_t *msg, void *payload, uint8_t len)
    {
        if(!_isBusy)
        {
            if(len==sizeof(respTopMsg_t))
            {
                respTopMsg_t *m=(respTopMsg *)payload;
                if(m->dst==(nx_uint8_t)TOS_NODE_ID)
                {
                    printf("%u %u\n", m->origem, m->destino);
                }
            }
            if(len==sizeof(respDadoMsg_t))
            {
                respDadoMsg_t *m=(respDadoMsg *)payload;
                if(m->dst==(nx_uint8_t)TOS_NODE_ID)
                {
                    printf("%u: %u %u\n", m->origem, m->temp, m->lum);
                }
            }
        }

        if(_isBusy) report.problem();
    }

	event void AMSend.sendDone(message_t *msg, error_t error)
	{
		if (error == SUCCESS)
      		report_sent();
    	else
      		report_problem();
        
        if (& _packet == msg) {
            _isBusy = FALSE;
        }	
	}
}