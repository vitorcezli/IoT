#ifndef NODES_COMM_H
#define NODES_COMM_H

enum {
	/* Tamanho do campo extras, apenas usado no recebimento de dados. */
	TAM_EXTRAS = 10,
	/* Definição de canal de comunição em redes, */
	AM_RADIO_SENSE_MSG = 6,
	/* Tempo em miliseg para encerrar o recebimento de dados e fechar o pacote para envio para interface gráfica. */
	TIMER_PERIOD_MILLI = 250
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
