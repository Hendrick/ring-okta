# ring-okta

[![Build Status](https://travis-ci.org/Hendrick/ring-okta.svg?branch=master)](https://travis-ci.org/Hendrick/ring-okta)

Ring middleware for Okta Single Sign-on.

## Installation

### Leiningen

```
[ring-okta "0.1.6"]
```

### Gradle

```
compile "ring-okta:ring-okta:0.1.6"
```

### Maven

```
<dependency>
  <groupId>ring-okta</groupId>
  <artifactId>ring-okta</artifactId>
  <version>0.1.6</version>
</dependency
```

### Okta SAML Toolkit Dependency

Since Okta doesn't publish the SAML Toolkit for Java, you must
download it
[here](https://support.okta.com/entries/25009573-Current-SAML-Toolkit-for-Java-Version).
You then must `mvn install` it to your local maven repository. Check
the `project.clj` for the version of the SAML Toolkit to download from Okta.

## Usage

```
(ns com.company.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.okta :refer [wrap-okta okta-routes]]))

(defroutes company-routes
  (GET "/" [] "<h1>Hello World</h1>")

  okta-routes

  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> company-routes
      (wrap-okta {:okta-home "https://company.okta.com"})))
```

## Documentation

- [API Docs](http://Hendrick.github.io/ring-okta/index.html)

The documentation is built with [codox](https://github.com/weavejester/codox) (`lein doc`) and published to
the [gh-pages](https://github.com/Hendrick/ring-okta/tree/gh-pages) branch.

## Test Coverage

The test coverage summary is built with [cloverage](https://github.com/lshift/cloverage) (`lein cloverage`).

## Development

As described in **Usage** above, the Okta SAML Toolkit must be downloaded and installed to your local maven repository. When updating this dependency, here is how you can install the downloaded jar:

```
$ mvn install:install-file -Dfile=saml-toolkit.jar -DgroupId=com.okta -DartifactId=saml-toolkit -Dpackaging=jar -Dversion=<version> -DcreateChecksum=true -DupdateReleaseInfo=true -DgeneratePom=true -DlocalRepositoryPath=/path/to/localRepo
```

### Generating API Docs

The `lein doc` command is configured in `project.clj` to output documentation to `../ring-okta-doc`. This should be configured to be the `gh-pages` branch so the API docs can be hosted on GitHub.

```
~/code
  |- ring-okta
  |- ring-okta-doc
  ...
```

Once `lein doc` is run in `ring-okta`, you can commit the changes in `ring-okta-doc` to the `gh-pages` branch of this repository.

## License

Copyright © 2015 Hendrick Automotive Group

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
