# Dropwizard Riemann Bundle [![Travis build status](https://travis-ci.org/phaneesh/riemann-bundle.svg?branch=master)](https://travis-ci.org/phaneesh/riemann-bundle)

This bundle simplifies integrating dropwizard metrics with [Riemann](http://riemann.io/).
This bundle compiles only on Java 8.
 
## Dependencies
* [metrics3-riemann-reporter](https://github.com/riemann/riemann-java-client/tree/master/metrics3-riemann-reporter) 0.4.1 

## Usage
The bundle integrates dropwizard metrics with [Riemann](http://riemann.io/) with a simple configuration. 
 
### Build instructions
  - Clone the source:

        git clone github.com/phaneesh/riemann-bundle

  - Build

        mvn install

### Maven Dependency
Use the following repository:
```xml
<repository>
    <id>clojars</id>
    <name>Clojars repository</name>
    <url>https://clojars.org/repo</url>
</repository>
```
Use the following maven dependency:
```xml
<dependency>
    <groupId>io.dropwizard</groupId>
    <artifactId>dropwizard-riemann</artifactId>
    <version>1.0.5-1</version>
</dependency>
```

### Using Riemann bundle

#### Bootstrap
```java
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new RiemannBundle() {
            
            @Override
            public RiemannConfig getRiemannConfiguration(Configuration configuration) {
                ...
            }
        });
    }
```

### Configuration
```
riemann:
  host: my.riemann.host
  port: 5556
  prefix: mycompany.myenvironment.myservice
  pollingInterval: 60 
  tags:
    - mytag1
    - mytag2
    - mytag3
```

LICENSE
-------

Copyright 2016 Phaneesh Nagaraja <phaneesh.n@gmail.com>.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.