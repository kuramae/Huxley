Directory structure
=========
- androidsrc contains old android source code. We will resume the development there and make a lightweight client for android. Most of the code written there should go to the server
- huxley-server contains the server. It exposes a REST api to the rest of the world and it builds on Jersey
- ios-client contains the ios client. 


Build and Run the server
=========
Prerequisites
-----------
We need Java and Maven in the PATH. Let us also assume we have a working directory:
```
export WORKINGDIR=~/Dev
```
Assuming Java is installed Maven can be obtained by executing:
```
curl http://mirror.catn.com/pub/apache/maven/maven-3/3.1.1/binaries/apache-maven-3.1.1-bin.tar.gz | tar xvz | mv maven* $WORKINGDIR/maven
export PATH="$WORKINGDIR/maven:$PATH"
```
Now check that 
```
mvn -v
```
returns something meaningful and you are ready to go.
