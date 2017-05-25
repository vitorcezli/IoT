#include "nodes_comm.h"

configuration NodeAppC
{
	/* Do Nothing. */	
}

implementation
{
	/* General Use */
	components MainC, NodeC as App;
	components new TimerMilliC() as TopoTimer;
	components new TimerMilliC() as DadoTimer;
	components LedsC;
	components new DemoSensorC() as PhotoC;
	components new DemoSensorC() as TempC;
	
	
	/* Radio Communcation Use*/
  	components ActiveMessageC;
  	components new AMSenderC(AM_REQ_TOPOLOGIA) as ReqTopSender;
  	components new AMReceiverC(AM_REQ_TOPOLOGIA) as ReqTopReceiver;
  	
  	components new AMSenderC(AM_RESP_TOPOLOGIA) as RespTopSender;
  	components new AMReceiverC(AM_RESP_TOPOLOGIA) as RespTopReceiver;
  	
  	components new AMSenderC(AM_REQ_DADOS) as ReqDadoSender;
  	components new AMReceiverC(AM_REQ_DADOS) as ReqDadoReceiver;
  	
  	components new AMSenderC(AM_RESP_DADOS) as RespDadoSender;
  	components new AMReceiverC(AM_RESP_DADOS) as RespDadoReceiver;
  	
  	/* Interface com NodeC */
  	App.Boot -> MainC;
  	App.Leds -> LedsC;
  	App.MilliTimerTop -> TopoTimer;
  	App.MilliTimerDad -> DadoTimer;
  	App.PSensor -> PhotoC;
  	App.TSensor -> TempC;
  	
  	/* Interfaces de comunicação. */
 	App.AMControl -> ActiveMessageC;
 	
 	App.AMSendReqTop -> ReqTopSender;
  	App.PacketReqTop -> ReqTopSender;
  	App.AMPacketReqTop -> ReqTopSender;
  	App.ReceiveReqTop -> ReqTopReceiver;
  	
  	App.AMSendRespTop -> RespTopSender;
  	App.PacketRespTop -> RespTopSender;
  	App.AMPacketRespTop -> RespTopSender;
  	App.ReceiveRespTop -> RespTopReceiver;
  	
  	App.AMSendReqDado -> ReqDadoSender;
  	App.PacketReqDado -> ReqDadoSender;
  	App.AMPacketReqDado -> ReqDadoSender;
  	App.ReceiveReqDado -> ReqDadoReceiver;
  	
  	App.AMSendRespDado -> RespDadoSender;
  	App.PacketRespDado -> RespDadoSender;
  	App.AMPacketRespDado -> RespDadoSender;
  	App.ReceiveRespDado -> RespDadoReceiver;
  	
}