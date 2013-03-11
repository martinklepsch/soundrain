# soundrain

A Clojure library designed to ... well, that part is up to you.

## bookmarklet

```js
javascript:(function(){var newScript = document.createElement('script');newScript.src ='https://dl.dropbox.com/u/453692/sc.js?dummy='
                     + Math.random();
    document.body.appendChild(newScript);
})();
```
[sc.js](https://dl.dropbox.com/u/453692/sc.js) kann mit dem Code oben, den man zu einem
Bookarklet machen kann, eingebunden werden. Das geladene Javascript fügt unter jedem Track
einen Downloadbutton ein (TODO: gucken ob es schon einen gibt) und linkt diesen zu einer
Funktion die in einem seperaten Script Tag eingebunden wird.


## Usage

Start the local server with `lein ring server`.

## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
