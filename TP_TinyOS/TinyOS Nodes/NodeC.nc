
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
        interface Package;
        interface AMPackage;
        interface AMSend; 
        interface SplitControl as AMControl;
        interface Receive;
    }    
    
}


implementation{
    nx_uint8_t meuPai;
    bool busy=FALSE;
    bool visitado=FALSE;
    ReqTopologiaMsg_t m1;
    RespTopologiaMsg_t m2;
    ReqDados_t m3;
    RespDados_t m4;
    
    //receber -> verificar se é resposta
    //se não for resposta, mandar para os outros
    //ler a temperatura e a luminosidade
    //responder
    
    event void Boot.booted() {
      call AMControl.start();
    }

    event void AMControl.startDone(error_t err) {
        if (err == SUCCESS) {
            call MilliTimer.startPeriodic(250);
        }
    }
    event void AMControl.stopDone(error_t err) {}

    /* Evento de Reconhecimento de Topologia */
    event void Receive.receive_req_top(ReqTopologiaMsg_t *m){
        if(visitado==FALSE){
            meuPai=m->src;
            visitado=TRUE; //para nao visitar novamente

            //RESPONDENDO
            m2.tam=10;
            m2.src=TOS_MOTE_ID;
            m2.dst=meuPai;
            m2.id_req=m->id_req;
            m2.pai=meuPai;
            m2.destino=TOS_MOTE_ID;
            call AMSend.send_resp_top(&m2);

            //ENVIANDO
            m1.tam=6;
            m1.src=TOS_MOTE_ID;
            m1.id_req=m->id_req;
            call AMSend.send_req_top(&m1);
        }
    }

    event void Receive.receive_resp_top(RespTopologiaMsg_t *m){
        if(m->dst==TOS_MOTE_ID){
            m2=*m;
            m2.src=TOS_MOTE_ID;
            m2.dst=meuPai;
            call AMSend_send_resp_top(&m2);
        }
    }
    
    /* Evento de Requisição de dados. */
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
            if(call AMSend.send_resp_dados(&m3) == SUCESS){
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
      busy = FALSE;
    }
  }

}
