#ifndef NODES_COMM_H
#define NODES_COMM_H

typedef nx_struct reqTopologiaMsg {
  nx_uint8_t tam;
  nx_uint8_t tipo = 0x01;
  nx_uint8_t src;
  nx_uint8_t dst;
  nx_uint16_t id_req;
} reqTopologiaMsg_t;

typedef nx_struct respTopologiaMsg {
  nx_uint8_t tam;
  nx_uint8_t tipo = 0x0;
  nx_uint8_t src;
  nx_uint8_t dst;
  nx_uint16_t id_req;
  nx_uint16_t pai;
  nx_uint16_t destino;
} respTopologiaMsg_t;

typedef nx_struct reqDadosMsg {
  nx_uint8_t tam;
  nx_uint8_t tipo = 0x03;
  nx_uint8_t src;
  nx_uint8_t dst;
  nx_uint16_t id_req;
  nx_uint16_t pai;
  nx_uint16_t destino;
} reqDadosMsg_t;

typedef nx_struct respDadosMsg {
  nx_uint8_t tam;
  nx_uint8_t tipo = 0x04;
  nx_uint8_t src;
  nx_uint8_t dst;
  nx_uint16_t id_req;
  nx_uint16_t lum;
  nx_uint16_t temp;
  nx_uint16_t origem;
  nx_uint16_t [TAM_EXTRAS] extras;	 
} respDadosMsg_t;

enum {
  TAM_EXTRAS = 10,
};

#endif
