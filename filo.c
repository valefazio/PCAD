#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

void initializza_bacchette();
void *filosofo(void*);
void pensa(int);
void prendi_bacchetta_sinistra(int);
void prendi_bacchetta_destra(int);
void mangia(int);
void rilascia_bacchette(int);

const int numero_pasti = 5;
const int bacchette_disponibili = 5;
pthread_mutex_t bacchette[5];
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

int main() {
    initializza_bacchette();

    pthread_t filo[bacchette_disponibili];    //creazione 5 thread, uno per filosofo
    for(int i=0; i<bacchette_disponibili; i++){
        int* n = &i;
        pthread_create(&filo[i], NULL, filosofo, n);
    }
    for(int i=0; i<bacchette_disponibili; i++){
        pthread_join(filo[i], NULL);
    }

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
        prendi_bacchetta_sinistra(n);
        prendi_bacchetta_destra(n);
        mangia(n);
        rilascia_bacchette(n);
        times++;
    }
}

void pensa(int n) {
    printf("Filosofo %d: sta pensando\n", n);
    sleep(1);
}

void prendi_bacchetta_sinistra(int n) {
    pthread_mutex_lock(&bacchette[n]);
    printf("Filosofo %d: ha la bacchetta sinistra\n", n);
}

void prendi_bacchetta_destra(int n) {
    pthread_mutex_lock(&bacchette[(n+1)%bacchette_disponibili]);
    printf("Filosofo %d: ha la bacchetta destra\n", n);
}

void mangia(int n) {
    printf("Filosofo %d: sta mangiando\n", n);
    sleep(1);
}

void rilascia_bacchette(int n) {
    /*pthread_mutex_lock(&mutex);
    bacchette_disponibili += 2;
    pthread_cond_signal(&cond);*/
    pthread_mutex_unlock(&bacchette[n]);
    pthread_mutex_unlock(&bacchette[(n+1)%bacchette_disponibili]);
    printf("Filosofo %d: ha rilasciato le sue due bacchetta\n", n);
}