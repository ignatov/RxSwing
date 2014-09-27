/**
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.swing.sources;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observables.SwingObservable;
import rx.subscriptions.SwingSubscriptions;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public enum WindowStateEventSource {
    ; // no instances

    /**
     * @see rx.observables.SwingObservable#fromWindowStateEvents
     */
    public static Observable<WindowEvent> fromWindowStateEventsOf(final Window window) {
        return Observable.create(new OnSubscribe<WindowEvent>() {
            @Override
            public void call(final Subscriber<? super WindowEvent> subscriber) {
                SwingObservable.assertEventDispatchThread();
                final WindowStateListener listener = new WindowStateListener() {
                    @Override
                    public void windowStateChanged(WindowEvent e) {
                        subscriber.onNext(e);
                    }
                };
                window.addWindowStateListener(listener);
                subscriber.add(SwingSubscriptions.unsubscribeInEventDispatchThread(new Action0() {
                    @Override
                    public void call() {
                        window.removeWindowStateListener(listener);
                    }
                }));
            }
        });
    }
}
