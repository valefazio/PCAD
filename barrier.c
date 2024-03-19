#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "my_barrier.h"


void* action(void*);
const int n_threads = 5;
my_barrier barrier;

int main() {
    pthread_t threads[5];
    
    for (int i = 0; i < n_threads; i++) {
        void* arg = malloc(sizeof(int));
        *(int*)arg = i;
        pthread_create(&threads[i], NULL, action,  arg);
    }

    pthread_my_barrier_init(&barrier, n_threads);

    pthread_join(threads[0], NULL);
    pthread_join(threads[1], NULL);
    pthread_join(threads[2], NULL);
    pthread_join(threads[3], NULL);
    pthread_join(threads[4], NULL);

    return 0;
}

void *action(void* i) {
    printf("Thread %d entra\n", *(int*) i);
    sleep(1);
    pthread_my_barrier_wait(&barrier);
    printf("Thread %d esce\n", *(int*) i);
}

unsigned int pthread_my_barrier_init(my_barrier *mb, unsigned int v) {
    if(v == 0)
        return -1;
    mb->vinit = v;
    mb->val = 0;
    pthread_mutex_init(&mb->lock, NULL);
    return 0;
}

unsigned int pthread_my_barrier_wait(my_barrier *mb) {
    pthread_mutex_lock(&mb->lock);
    mb->val++;
    if(mb->val == mb->vinit) {
        mb->val = 0;
        pthread_mutex_unlock(&mb->lock);
        pthread_cond_broadcast(&mb->varcond);
    } else {
        pthread_cond_wait(&mb->varcond, &mb->lock);
        pthread_mutex_unlock(&mb->lock);
    }
    return 0;
}