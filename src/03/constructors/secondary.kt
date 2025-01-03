package `03`.constructors

import java.net.URI

/*
Java implementation:

import java.net.URI;

public class Downloader {
    public Downloader(String url) {
    // some code
    }
    public Downloader(URI uri) {
    // some code
    }
}
*/

open class Downloader {
    constructor(url: String?) { // some code
    }
    constructor(uri: URI?) {
    // some code
    }
}

// If you want to extend this class, you can declare the same constructors
class MyDownloader : Downloader {
    constructor(url: String?) : super(url) { // Calling superclass constructors
        // ...
    }
    constructor(uri: URI?) : super(uri) {  // ...
    }
}

// You also have an option to call another constructor of your own class from a constructor, using the this() keyword
class MyDownloader2 : Downloader {
    constructor(url: String?) : this(URI(url))
    constructor(uri: URI?) : super(uri)
}