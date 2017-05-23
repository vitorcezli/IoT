
#include "nodes_comm.h"

configuration NodeAppC
{
	/* Do Nothing. */	
}

implementation
{
	/* General Use */
	components MainC, NodeC as App;
	components new TimerMilliC();
	components LedsC;
	components new DemoSensorC() as Sensor;
	
	/* Radio Communcation Use*/
  	components ActiveMessageC;
  	components new AMSenderC(AM_RADIO_SENSE_MSG);
  	components new AMReceiverC(AM_RADIO_SENSE_MSG);
  	

  	App.Boot -> MainC;
  	App.Leds -> LedsC;
  	App.MilliTimer -> TimerMilliC;
  	App.Sensor -> Sensor;
  	
 	App.AMSend -> AMSenderC;
  	App.Packet -> AMSenderC;
  	App.AMPacket -> AMSenderC;
  	App.Receive -> AMReceiverC;
  	App.AMControl -> ActiveMessageC;
  	
  	
  	//App.Read -> DemoSensorC;
}