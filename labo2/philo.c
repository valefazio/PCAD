#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "my_semaphore.h"
// Compile as:	gcc my_semaphore.c philo.c

void initializza_bacchette();
void *filosofo(void *);
void pensa(int);
void prendi_bacchette(int);
void mangia(int);
void rilascia_bacchette(int);

const int numero_pasti = 5;
const int bacchette_disponibili = 5;
my_semaphore bacchette[5];
my_semaphore salaDAttesa;

int main() {
	my_sem_init(&salaDAttesa, 4);
	initializza_bacchette();

	pthread_t filo[bacchette_disponibili];
	for (int i = 0; i < bacchette_disponibili; i++) {
		void *arg = malloc(sizeof(int));
		*(int *)arg = i;
		pthread_create(&filo[i], NULL, filosofo, arg);
	}

	for (int i = 0; i < bacchette_disponibili; i++) {
		pthread_join(filo[i], NULL);
	}

	my_sem_destroy(&salaDAttesa);
	for (int i = 0; i < bacchette_disponibili; i++) {
		my_sem_destroy(&bacchette[i]);
	}
	
	return 0;
}

void initializza_bacchette() {
	for (int i = 0; i < bacchette_disponibili; i++)
		my_sem_init(&bacchette[i], 1);
}

void *filosofo(void *i) {
	int times = 0;
	int n = *(int *)i;
	while (times < numero_pasti) {
		my_sem_wait(&salaDAttesa);
		pensa(n);
		prendi_bacchette(n);
		mangia(n);
		rilascia_bacchette(n);
		times++;
		my_sem_signal(&salaDAttesa);
	}
}

void pensa(int n) {
	printf("Filosofo %d: sta pensando\n", n);
	sleep(1);
}

void prendi_bacchette(int n) {
	my_sem_wait(&bacchette[n]);
	printf("Filosofo %d: ha la bacchetta sinistra\n", n);
	//sleep(1);
	my_sem_wait(&bacchette[(n + 1) % bacchette_disponibili]);
	printf("Filosofo %d: ha la bacchetta destra\n", n);
}

void mangia(int n) {
	printf("Filosofo %d: sta mangiando\n", n);
	sleep(1);
}

void rilascia_bacchette(int n) {
	my_sem_signal(&bacchette[n]);
	my_sem_signal(&bacchette[(n + 1) % bacchette_disponibili]);
	printf("Filosofo %d: ha rilasciato le sue due bacchetta\n", n);
}