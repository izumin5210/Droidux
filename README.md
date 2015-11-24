# Droidux
[![Build Status](https://travis-ci.org/izumin5210/Droidux.svg)](https://travis-ci.org/izumin5210/Droidux)
[![Download](https://api.bintray.com/packages/izumin5210/maven/droidux/images/download.svg) ](https://bintray.com/izumin5210/maven/droidux/_latestVersion)
[![Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/izumin5210/Droidux/blob/master/LICENSE.md)

Droidux is "predictable state container" implementation, inspired by **[Redux][redux]**.

## Features
Droidux is influenced by [Three principles][three-principles] of Redux.

> * Single source of truth
>     - The state of your whole application is stored in an object tree inside a single store.
> * State is read-only
>     - The only way to mutate the state is to emit an action, an object describing what happened.
> * Mutations are written as pure functions
>     - To specify how the state tree is transformed by actions, you write pure reducers.
>
> [Three Principles | Redux][three-principles]

Features of Droidux are following:

* All mutations can be observed via rx.Observable from [RxJava][rxjava]
* All mutations are automatically notified to views via [Data Binding][databinding]

### Data flow
![Droidux data flow](droidux.png)

see also: [Introduction to Redux // Speaker Deck](https://speakerdeck.com/axross/introduction-to-redux) (in Japanese)

## Installation
Add to your project build.gradle file:

```groovy
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
  compile 'info.izumin.android:droidux:0.3.0'
  apt 'info.izumin.android:droidux:0.3.0'
}
```

And also you need to setup [Data Binding][databinding].


## Usage
### Quick example

```java
/**
 * This is a state class.
 * It can be as simple as possible implementation, like POJO, or immutable object. 
 */
public class Counter {
    private final int count;

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}

/**
 * This is a reducer class.
 * It should be applied @Reducer annotation is given a state class as an argument.
 * It describe whether the reducer should handle which actions.
 */
@Reducer(Count.class)
public class CountReducer {

    /**
     * This is a method to handle actions.
     * It should be applied @Dispatchable annotation is given an action class as ano argument.
     * It describe how to transform the state into the next state when dispatched actions.
     * It should return the next state instance, and it is preferred instantiate the new state.
     *
     * This example handle IncrementCountAction,
     + and it returns new counter instance that state is incremented.
     */
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

/**
 * They are action classes. They should extend Action class.
 */
public class IncrementCountAction extends Action {}
public class DecrementCountAction extends Action {}
public class ClearCountAction extends Action {}


// Instantiate a Droidux store holding the state of your app.
// Its class is generated automatically from Reducer class.
// 
// The instantiating should use Builder class,
// and it should register a reducer instance and an initial state.
// 
// Its APIs in this example are following:
// - rx.Observable<Action> dispatch(Action action)
// - rx.Observable<Counter> observe()
// - Counter getState()
DroiduxCounterStore store = new DroiduxCounterStore.Builder()
        .addReducer(new CounterReducer())
        .addInitialState(new Counter(0))
        .build();                                       // Counter: 0

// You can observe to the updates using RxJava interface. 
store.observe((counter) -> Log.d(TAG, counter.toString()));

// The only way to mutate the internal state is to dispatch an action.
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

### Undo / Redo

```java
@Undoable
@Reducer(TodoList.class)
class TodoListReducer {
    @Dispatchable(AddTodoAction.class)
    public TodoList addTodo(TodoList state, AddTodoAction action) {
        // ...
    }

    @Dispatchable(CompleteTodoAction.class)
    public TodoList completeTodo(TodoList state, CompleteTodoAction action) {
        // ...
    }
}

class TodoList extends ArrayList<TodoList.Todo> implements UndoableStore {
    @Override
    public TodoList clone() {
        // ...
    }

    public static Todo {
        // ...
    }
}

class AddTodoAction extends Action{
    // ...
}

class CompleteTodoAction extends Action{
    // ...
}


DroiduxTodoListStore store = new DroiduxTodoListStore.Builder()
        .addReducer(new TodoListReducer())
        .addInitialState(new TodoList())
        .build();

store.dispatch(new AddTodoAction("item 1")).subscribe();        // ["item 1"]
store.dispatch(new AddTodoAction("item 2")).subscribe();        // ["item 1", "item 2"]
store.dispatch(new AddTodoAction("item 3")).subscribe();        // ["item 1", "item 2", "item 3"]
store.dispatch(new CompleteTodoAction("item 2")).subscribe();   // ["item 1", "item 3"]
store.dispatch(new AddTodoAction("item 4")).subscribe();        // ["item 1", "item 3", "item 4"]

store.dispatch(new UndoAction(DroiduxTodoListStore.class)).subscribe();
// => ["item 1", "item 3"]

store.dispatch(new UndoAction(DroiduxTodoListStore.class)).subscribe();
// => ["item 1", "item 2", "item 3"]

store.dispatch(new RedoAction(DroiduxTodoListStore.class)).subscribe();
// => ["item 1", "item 3"]
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

### Bindable methods

* `Store#getState()`
* `Store#isUndoalble()`     // when the reducer is annotated with `@Undoable`
* `Store#isRedoalble()`     // when the reducer is annotated with `@Undoable`


## Examples

* [TodoMVC](https://github.com/izumin5210/Droidux/tree/master/examples/todomvc)
* [Todos with Undo](https://github.com/izumin5210/Droidux/tree/master/examples/todos-with-undo)


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
[rxjava]: https://github.com/ReactiveX/RxJava
[three-principles]: http://rackt.org/redux/docs/introduction/ThreePrinciples.html
[databinding]: http://developer.android.com/tools/data-binding/guide.html