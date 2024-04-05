#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "my_semaphore.h"
// Compile as:	gcc my_semaphore.c bus.c

void sale(int);
void scende(int);
void *passeggero(void *);
void *drive(void *);

const int numero_posti = 8;
const int numero_passeggeri = 24;
int posti_occupati = 0;
my_semaphore siParte;
pthread_create(&bus, NULL, drive, NULL);

int main() {
    my_sem_init(&siParte, numero_posti - 1);

    pthread_t passeggeri[numero_passeggeri];
    for (int i = 0; i < numero_passeggeri; i++) {
        void *arg = malloc(sizeof(int));
        *(int *)arg = i;
        pthread_create(&passeggeri[i], NULL, passeggero, arg);
    }

    for (int i = 0; i < numero_passeggeri; i++) {
        pthread_join(passeggeri[i], NULL);
    }

    my_sem_destroy(&siParte);
    return 0;
}

void *passeggero(void *i) {
    int n = *(int *)i;
	sale(n);
}

void *drive(void *arg) {
	while (1) {
		printf("Il bus è fermo\n");
		sleep();
		printf("Il bus parte\n");
    	wait(2);
	}
}

void sale(int n) {
	my_sem_wait(&siParte);
    posti_occupati++;
	if(posti_occupati == numero_posti)
		notify(&bus);
    printf("Passeggero %d sale sul bus\n", n);
    scende(n);
}

void scende(int n) {
    printf("Passeggero %d scende dal bus\n", n);
    posti_occupati--;
	if(posti_occupati == 0)
		my_sem_signal(&siParte);
}