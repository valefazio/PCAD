#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

void initializza_bacchette();
void *filosofo(void*);
void pensa(int);
void prendi_bacchette(int);
void mangia(int);
void rilascia_bacchette(int);

const int numero_pasti = 5;
const int bacchette_disponibili = 5;
pthread_mutex_t bacchette[5];
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

int main() {
    initializza_bacchette();

    pthread_t filo[bacchette_disponibili];
    /* pthread_create(&filo[0], NULL, filosofo, (void*) &(int){0});
    pthread_create(&filo[1], NULL, filosofo, (void*) &(int){1});
    pthread_create(&filo[2], NULL, filosofo, (void*) &(int){2});
    pthread_create(&filo[3], NULL, filosofo, (void*) &(int){3});
    pthread_create(&filo[4], NULL, filosofo, (void*) &(int){4}); */
    for (int i = 0; i < bacchette_disponibili; i++) {
		void* arg = malloc(sizeof(int));
		*(int*)arg = i;
        pthread_create(&filo[i], NULL, filosofo,  arg);
    }
    
    pthread_join(filo[0], NULL);
    pthread_join(filo[1], NULL);
    pthread_join(filo[2], NULL);
    pthread_join(filo[3], NULL);
    pthread_join(filo[4], NULL);

	pthread_mutex_destroy(&bacchette[0]);
    pthread_mutex_destroy(&bacchette[1]);
    pthread_mutex_destroy(&bacchette[2]);
    pthread_mutex_destroy(&bacchette[3]);
    pthread_mutex_destroy(&bacchette[4]);
    
    return 0;
}

void initializza_bacchette() {
    for (int i = 0; i < bacchette_disponibili; i++) {
        pthread_mutex_init(&bacchette[i], NULL);
    }
}

void *filosofo(void* i) {
    int times = 0;
    int n = *(int*)i;
    while (times < numero_pasti) {
        pensa(n);
        prendi_bacchette(n);
        mangia(n);
        rilascia_bacchette(n);
        times++;        
    }
}

void pensa(int n) {
    printf("Filosofo %d: sta pensando\n", n);
    sleep(1);
}

void prendi_bacchette(int n) {
    pthread_mutex_lock(&bacchette[n]);
    printf("Filosofo %d: ha la bacchetta sinistra\n", n);
    sleep(2);
    pthread_mutex_lock(&bacchette[(n+1)%bacchette_disponibili]);
    printf("Filosofo %d: ha la bacchetta destra\n", n);
}

void mangia(int n) {
    printf("Filosofo %d: sta mangiando\n", n);
    sleep(1);
}

void rilascia_bacchette(int n) {
    pthread_mutex_unlock(&bacchette[n]);
    pthread_mutex_unlock(&bacchette[(n+1)%bacchette_disponibili]);
    printf("Filosofo %d: ha rilasciato le sue due bacchetta\n", n);
}