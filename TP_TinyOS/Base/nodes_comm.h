#ifndef NODES_COMM_H
#define NODES_COMM_H

enum {
	/* Tamanho do campo extras, apenas usado no recebimento de dados. */
	TAM_EXTRAS = 10,
	/* Definição de canal de comunição em redes, */
	AM_REQ_TOPOLOGIA = 0x01,
	AM_RESP_TOPOLOGIA = 0x02,
	AM_REQ_DADOS = 0x03,
	AM_RESP_DADOS = 0x04,
	
	/* Tempo em miliseg para encerrar o recebimento de dados e fechar o pacote para envio para interface gráfica. */
	TIMER_PERIOD_MILLI = 250,

	AM_RADIO_SENSE_MSG = 7
};

typedef nx_struct reqTopMsg {
	nx_uint8_t tam;
	nx_uint8_t tipo;
	nx_uint8_t src;
	nx_uint8_t dst;
	nx_uint16_t id_req;
} reqTopMsg_t;

typedef nx_struct respTopMsg {
	nx_uint8_t tam;
	nx_uint8_t tipo;
	nx_uint8_t src;
	nx_uint8_t dst;
	nx_uint16_t id_req;
	nx_uint16_t pai;
	nx_uint16_t origem;
	nx_uint16_t destino;
} respTopMsg_t;

typedef nx_struct reqDadoMsg {
	nx_uint8_t tam;
	nx_uint8_t tipo;
	nx_uint8_t src;
	nx_uint8_t dst;
	nx_uint16_t id_req;
} reqDadoMsg_t;

typedef nx_struct respDadoMsg {
	nx_uint8_t tam;
	nx_uint8_t tipo;
	nx_uint8_t src;
	nx_uint8_t dst;
	nx_uint16_t id_req;
	nx_uint16_t origem;
	nx_uint16_t lum;
	nx_uint16_t temp;
	nx_uint8_t extras [TAM_EXTRAS];
} respDadoMsg_t;

#endif /* NODES_COMM_H */
