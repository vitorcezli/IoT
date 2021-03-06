#include "nodes_comm.h"
#include "printf.h"

configuration BaseNodeAppC {}

implementation {
  components MainC, BaseNodeC as App, LedsC, new DemoSensorC();
  components ActiveMessageC;
  components new AMSenderC(AM_RADIO_SENSE_MSG);
  components new AMReceiverC(AM_RADIO_SENSE_MSG);
  components new TimerMilliC() as Timer0;
  components new TimerMilliC() as Timer1;
  
  App.Boot -> MainC.Boot;
  
  App.Receive -> AMReceiverC;
  App.AMSend -> AMSenderC;
  App.AMControl -> ActiveMessageC;
  App.Leds -> LedsC;
  App.Timer0 -> Timer0;
  App.Timer1 -> Timer1; 
  App.Packet -> AMSenderC;
}
