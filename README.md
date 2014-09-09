# ring-okta

[![Dependency Status](https://www.versioneye.com/user/projects/540df18842c8d5146300000c/badge.svg)](https://www.versioneye.com/user/projects/540df18842c8d5146300000c)

Ring middleware for Okta Single Sign-on.

## Installation

### Leiningen

```
[ring/ring-okta "0.1.0"]
```

### Gradle

```
compile "ring:ring-okta:0.1.0"
```

### Maven

```
<dependency>
  <groupId>ring</groupId>
  <artifactId>ring-okta</artifactId>
  <version>0.1.0</version>
</dependency
```

## Usage

```
(ns com.company.core
  (:require [compojure.core :refer [defroutes GET]]
            [ring.middleware.okta :refer [wrap-okta okta-routes]]))

(defroutes company-routes
  (GET "/" [] "<h1>Hello World</h1>")
  okta-routes)

(def app
  (-> company-routes
      (wrap-okta {:okta-home "https://company.okta.com"})))
```

## Documentation

- [API Docs](http://Hendrick.github.io/ring-okta/ring.middleware.okta.html)

## License

Copyright © 2014 Hendrick Automotive Group

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
