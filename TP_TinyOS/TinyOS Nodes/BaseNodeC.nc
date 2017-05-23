#include "Timer.h"
#include "nodes_comm.h"
#include <stdlib.h>

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
    
    
    nx_uint16_t id_req_atual;
    msg_t m;
    
    //FILE *arestas=fopen("arestas.txt", "w");
    //FILE *dados=fopen("dados.txt", "w")

    //receber -> verificar se é resposta
    //se não for resposta, mandar para os outros
    //ler a temperatura e a luminosidade
    //responder

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
            msg_t* mr=(msg_t*)(call Packet.getPayload(&_packet, sizeof(msg_t)));
            if (mr == NULL) {
                return;
            }
            mr->src = (nx_uint8_t)TOS_NODE_ID;
            mr->tipo = 0x01;
            mr->id_req = id_req_atual;
            if (call AMSend.send(AM_BROADCAST_ADDR, & _packet, sizeof(msg_t)) == SUCCESS) {
                _isBusy = TRUE;
            }
        }
    }

    event void Timer1.fired() {
        id_req_atual++;
        if (!_isBusy) {
            msg_t* mr = (msg_t*)(call Packet.getPayload(& _packet, sizeof(msg_t)));
            if (mr == NULL) {
                return;
            }
            mr->src = (nx_uint8_t)TOS_NODE_ID;
            mr->tipo=0x03;
            mr->id_req=id_req_atual;
            if (call AMSend.send(AM_BROADCAST_ADDR,& _packet, sizeof(msg_t)) == SUCCESS) {
                _isBusy = TRUE;
            }
        }
    }

    event message_t* Receive.receive(message_t *ms, void *payload, uint8_t len)
    {
        if(len==sizeof(msg_t) && !_isBusy)
        {
            if(ms->tipo == 0x02) //resp top
            {
                if(mr->dst==TOS_NODE_ID)
                {
                    fprintf(arestas, "%d %d\n", mr->origem, mr->destino);
                }
            }
            else if(mr->tipo=0x04) //resp dados
            {
                if(mr->dst==TOS_NODE_ID)
                {
                    fprintf(dados, "%d %lf %lf\n", mr->origem, mr->temp, mr->lum);
                }
            }
        }
        return ms;
    }

	event void AMSend.sendDone(message_t *msg, error_t error){}
}