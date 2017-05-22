#ifndef NODES_COMM_H
#define NODES_COMM_H

enum {
  TAM_EXTRAS = 10,
  AM_RADIO_SENSE_MSG = 6,
  TIMER_PERIOD_MILLI = 1000
};

typedef nx_struct msg {
  nx_uint8_t tam;
  nx_uint8_t tipo;
  nx_uint8_t src;
  nx_uint8_t dst;
  nx_uint16_t id_req;
  nx_uint16_t origem;
  nx_uint16_t destino;
  nx_uint16_t lum;
  nx_uint16_t temp;
  nx_uint8_t extras [TAM_EXTRAS];
} msg_t;

#endif /* NODES_COMM_H */
