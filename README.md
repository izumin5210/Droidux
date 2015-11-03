# Droidux
Droidux is "predictable state container" implementation, inspired by **[Redux][redux]**.

## Usage

```java
public class Counter {
    private final int count;

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}

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

public class IncrementCountAction extends Action {}
public class DecrementCountAction extends Action {}
public class ClearCountAction extends Action {}

DroiduxCounterStore store = new DroiduxCounterStore.Builder()
        .addReducer(new CounterReducer())
        .addInitialState(new Counter(0))
        .build();


store.dispatch(new IncrementCountAction());
store.dispatch(new IncrementCountAction());
store.dispatch(new IncrementCountAction()); // Counter: 3

store.dispatch(new DecrementCountAction()); // Counter: 2

store.dispatch(new ClearCountAction());     // Counter: 0
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
