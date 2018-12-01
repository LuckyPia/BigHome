package com.example.bighome.RxBus;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private static  volatile  RxBus rxBus;
    private final Subject<Object> subject = PublishSubject.create().toSerialized();
    private Disposable disposable;

    private RxBus(){

    }
    public static  RxBus getInstance(){
        if(rxBus==null){
            synchronized (RxBus.class){
                if(rxBus==null){
                    rxBus=new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void post(Object ob){
        subject.onNext(ob);
    }
    public <T>Observable toObservable(Class<T> eventType){
        return subject.ofType(eventType);
    }

    public void subscribe(Class bean, Consumer consumer) {
        disposable = toObservable(bean).subscribe(consumer);
    }

    public void unSubscribe(){
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
