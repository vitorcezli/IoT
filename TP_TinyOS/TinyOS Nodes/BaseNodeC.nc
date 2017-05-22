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

        /* Sensores */
        interface Read<uint16_t> as TempSensor;

        /* Comunicacao de Radio */
        interface Package;
        interface AMPackage;
        interface AMSend;
        interface SplitControl as AMControl;
        interface Receive;
    }

}


implementation{
    //nx_uint8_t meuPai;
    message_t pkt;
    nx_uint16_t id_req_atual;
    bool busy=FALSE;
    msg m;
    FILE *arestas=fopen("arestas.txt", "w");
    FILE *dados=fopen("dados.txt", "w")

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
        if (!busy) {
            msg* mr=(msg*)(call Packet.getPayload(&pkt, sizeof(msg)));
            if (mr == NULL) {
                return;
            }
            mr->src=TOS_NODE_ID;
            mr->tipo=0x01;
            mr->id_req=id_req;
            if (call AMSend.send(AM_BROADCAST_ADDR,
            &pkt, sizeof(BlinkToRadioMsg)) == SUCCESS) {
                busy = TRUE;
            }
        }
    }

    event void Timer1.fired() {
        id_req++;
        if (!busy) {
            msg* mr=(msg*)(call Packet.getPayload(&pkt, sizeof(msg)));
            if (mr == NULL) {
                return;
            }
            mr->src=TOS_NODE_ID;
            mr->tipo=0x03;
            mr->id_req=id_req;
            if (call AMSend.send(AM_BROADCAST_ADDR,
            &pkt, sizeof(BlinkToRadioMsg)) == SUCCESS) {
                busy = TRUE;
            }
        }
    }

    event message_t* Receive.receive(message_t *ms, void *payload, uint8_t len)
    {
        if(len==sizeof(msg) && !busy)
        {
            if(mr->tipo==0x02) //resp top
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
    }

    /* Evento de Requisição de dados.
    event void Receive.receive_req_dados(ReqDados_t *m)
    {
        if(m->src==meuPai){
            //resposta ao pai
            m4.tam = 32; //nao sei o que vai ser
            m4.src = TOS_MOTE_ID;
            m4.dst = meuPai;
            m4.id_req = m->id_req;
            m4.dst=m->pai;
            m4.lum->PhotoC;
            m4.temp->TempC;
            m4.origem=TOS_MOTE_ID;
            if(call AMSend.send_resp_dados(&m4) == SUCESS){
              busy = true;
            }

            //mandar para os filhos
            m3.src=TOS_MOTE_ID;
            m3.id_req=m->id_req;
            if(call AMSend.send_resp_dados(&m4) == SUCESS){
              busy = true;
            }
        }
    }

    event void Receive.receive_resp_dados(RespDados_t *m){
        if(m->dst==TOS_MOTE_ID){
            m4.tam = m->tam; //nao sei o que vai ser
            m4.src = TOS_MOTE_ID;
            m4.dst = meuPai;
            m4.id_req = m->id_req;
            m4.dst=m->pai;
            if(call AMSend.send_resp_dados(&m4) == SUCESS){
              busy = true;
            }
        }
    }


      event void MilliTimer.fired() {
    call Read.read();
  }

  event void AMSend.sendDone(message_t* bufPtr, error_t error) {
    if (&M4 == bufPtr) {
      locked = FALSE;
    }
  }*/

}