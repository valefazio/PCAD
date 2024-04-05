#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "my_semaphore.h"
// Compile as:	gcc my_semaphore.c bus.c

#define SLEEPING 0
#define DRIVING 1

void sale(int);
void scende(int);
void *passeggero(void *);
void *drive(void *);

const int numero_posti = 4;
const int numero_passeggeri = 9;
const int numero_giri = 2;
int posti_occupati = 0;
my_semaphore siParte;
pthread_mutex_t sali_scendi;
pthread_mutex_t bus_mutex;

//gestione del caso in cui numero_passaggi non è multiplo di numero_posti
int numero_passeggeri_in_attesa = numero_passeggeri * numero_giri;

int bus_state = SLEEPING;

int main() {
    my_sem_init(&siParte, numero_posti );
	pthread_mutex_init(&sali_scendi, NULL);
	pthread_mutex_init(&bus_mutex, NULL);

	pthread_t bus;
	pthread_create(&bus, NULL, drive, NULL);

    pthread_t passeggeri[numero_passeggeri];
    for (int i = 0; i < numero_passeggeri; i++) {
        void *arg = malloc(sizeof(int));
        *(int *)arg = i;
        pthread_create(&passeggeri[i], NULL, passeggero, arg);
    }

    for (int i = 0; i < numero_passeggeri; i++)
        pthread_join(passeggeri[i], NULL);
    my_sem_destroy(&siParte);
	pthread_mutex_destroy(&sali_scendi);
	pthread_mutex_destroy(&bus_mutex);
    return 0;
}

void *passeggero(void *i) {
    int n = *(int *)i;
	for(int i = 0; i < numero_giri; i++)
		sale(n);
}

void *drive(void *arg) {
	while (1) {
		printf("Il bus è fermo\n");
		pthread_mutex_lock(&bus_mutex);
		bus_state = SLEEPING;
		pthread_mutex_unlock(&bus_mutex);
    	while(bus_state == SLEEPING) {}
		printf("Il bus parte\n");
		sleep(2);
	}
}

void sale(int n) {
	my_sem_wait(&siParte);
	pthread_mutex_lock(&sali_scendi);
    posti_occupati++;
	numero_passeggeri_in_attesa--;
    printf("Passeggero %d sale sul bus\n", n);
	pthread_mutex_unlock(&sali_scendi);

	if(posti_occupati == numero_posti || numero_passeggeri_in_attesa == 0) {
		pthread_mutex_lock(&bus_mutex);
		bus_state = DRIVING;
		pthread_mutex_unlock(&bus_mutex);
	} else
		while(bus_state == SLEEPING) {}
    scende(n);
}

void scende(int n) {
	while(bus_state == DRIVING) {}
	pthread_mutex_lock(&sali_scendi);
    printf("Passeggero %d scende dal bus\n", n);
    posti_occupati--;
	pthread_mutex_unlock(&sali_scendi);
	int i = 0;
	if(posti_occupati == 0) {
		while(i <= numero_posti - 1) {
			my_sem_signal(&siParte);
			i++;
		}
	}
}