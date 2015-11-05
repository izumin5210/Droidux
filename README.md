# Droidux
[![Build Status](https://travis-ci.org/izumin5210/Droidux.svg)](https://travis-ci.org/izumin5210/Droidux)
[![Download](https://api.bintray.com/packages/izumin5210/maven/droidux/images/download.svg) ](https://bintray.com/izumin5210/maven/droidux/_latestVersion)
[![Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/izumin5210/Droidux/blob/master/LICENSE.md)

Droidux is "predictable state container" implementation, inspired by **[Redux][redux]**.

## Installation
Add to your project build.gradle file:

```groovy
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {
  compile 'info.izumin.android:droidux:0.1.0'
  apt 'info.izumin.android:droidux:0.1.0'
}
```

## Usage
### Quick example

```java
// Implementation of state class
public class Counter {
    private final int count;

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}

// Implementation of reducer
@Reducer(Count.class)
public class CountReducer {
    @Dispatchable(IncrementCountAction.class)
    public Counter onIncrement(Counter state, IncrementCountAction action) {
        return new Counter(state.getCount() + 1);
    }

    @Dispatchable(DecrementCountAction.class)
    public Counter onDecrement(Counter state, DecrementCountAction action) {
        return new Counter(state.getCount() - 1);
    }

    @Dispatchable(ClearCountAction.class)
    public Counter onClear(Counter state, ClearCountAction action) {
        return new Counter(0);
    }
}

// Implementation of actions
public class IncrementCountAction extends Action {}
public class DecrementCountAction extends Action {}
public class ClearCountAction extends Action {}


// Instantiate store class 
DroiduxCounterStore store = new DroiduxCounterStore.Builder()
        .addReducer(new CounterReducer())
        .addInitialState(new Counter(0))
        .build();                                       // Counter: 0

store.dispatch(new IncrementCountAction()).subscribe(); // Counter: 1
store.dispatch(new IncrementCountAction()).subscribe(); // Counter: 2
store.dispatch(new IncrementCountAction()).subscribe(); // Counter: 3

store.dispatch(new DecrementCountAction()).subscribe(); // Counter: 2

store.dispatch(new ClearCountAction()).subscribe();     // Counter: 0
```

### Combined reducer/store

```java
@CombinedReducer({CounterReducer.class, TodoListReducer.class})
class RootReducer {
}


DroiduxRootStore store = new DroiduxRootStore.Builder()
        .addReducer(new CounterReducer())
        .addInitialState(new Counter(0))
        .addReducer(new TodoListReducer())
        .addInitialState(new TodoList())
        .addMiddleware(new Logger())
        .build();

store.dispatch(new IncrementCountAction()).subscribe();     // Counter: 1, Todo: 0
store.dispatch(new AddTodoAction("new task")).subscribe();  // Counter: 1, Todo: 1
```

### Middleware

```java
class Logger extends Middleware {
    @Override
    public Observable<Action> beforeDispatch(Action action) {
        Log.d("[prev counter]", String.valueOf(getCount()));
        Log.d("[action]", action.getClass().getSimpleName());
        return Observable.just(action);
    }

    @Override
    public Observable<Action> afterDispatch(Action action) {
        Log.d("[next counter]", String.valueOf(getCount()));
        return Observable.just(action);
    }

    private int getCount() {
        return ((DroiduxCounterStore) getStore()).getState().getCount();
    }
}

// Instantiate store class 
DroiduxCounterStore store = new DroiduxCounterStore.Builder()
        .addReducer(new CounterReducer())
        .addInitialState(new Counter(0))
        .addMiddleware(new Logger())        // apply logger middleware
        .build();                           // Counter: 0

store.dispatch(new IncrementCountAction()).subscribe();
// logcat:
// [prev counter]: 0
// [action]: IncrementCountAction
// [next counter]: 1

store.dispatch(new IncrementCountAction()).subscribe();
// logcat:
// [prev counter]: 1
// [action]: IncrementCountAction
// [next counter]: 2

store.dispatch(new ClearCountAction()).subscribe();
// logcat:
// [prev counter]: 2
// [action]: ClearCountAction
// [next counter]: 0
```

### Async action

```java
class FetchTodoListAction extends Action {
    public Observable<FetchTodoListAction> fetch() {
        return client.fetch()
                .map(todoList -> {
                    this.todoList = todoList;
                    return this;
                });
    }
    
    public TodoList getTodoList() {
        return todoList;
    }
}

new FetchTodoAction().fetch().flatMap(store::dispatch).subscribe();
```

## License

```
Copyright 2015 izumin5210

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[redux]: https://github.com/rackt/redux
