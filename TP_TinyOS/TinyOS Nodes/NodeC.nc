
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
    event void AM.stopDone(error_t err) {}
    
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
  }

}
