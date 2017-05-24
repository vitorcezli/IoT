
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
  	components new AMSenderC(AM_RADIO_SENSE_MSG);
  	components new AMReceiverC(AM_RADIO_SENSE_MSG);
  	
  	App.Boot -> MainC;
  	App.Leds -> LedsC;
  	App.MilliTimerTop -> TopoTimer;
  	App.MilliTimerDad -> DadoTimer;
  	App.PSensor -> PhotoC;
  	App.TSensor -> TempC;
  	
 	App.AMSend -> AMSenderC;
  	App.Packet -> AMSenderC;
  	App.AMPacket -> AMSenderC;
  	App.Receive -> AMReceiverC;
  	App.AMControl -> ActiveMessageC;
  	
}