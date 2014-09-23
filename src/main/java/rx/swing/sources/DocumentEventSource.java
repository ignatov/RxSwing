package rx.swing.sources;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observables.SwingObservable;
import rx.subscriptions.SwingSubscriptions;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public enum DocumentEventSource {
    ;

    public static Observable<DocumentEvent> fromDocumentEvents(final Document document) {
        return Observable.create(new Observable.OnSubscribe<DocumentEvent>() {
            @Override
            public void call(final Subscriber<? super DocumentEvent> subscriber) {
                SwingObservable.assertEventDispatchThread();
                final DocumentListener listener = new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        subscriber.onNext(e);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        subscriber.onNext(e);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        subscriber.onNext(e);
                    }
                };
                document.addDocumentListener(listener);

                subscriber.add(SwingSubscriptions.unsubscribeInEventDispatchThread(new Action0() {
                    @Override
                    public void call() {
                        document.removeDocumentListener(listener);
                    }
                }));
            }
        });
    }
}
