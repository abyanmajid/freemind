freemind but it actually builds on mac !

you'll need java 17 and apache ant.

```sh
# brew
brew install ant

# java via sdkman
curl -s "https://get.sdkman.io/" | bash
sdk list java # list java versions

# install one e.g.,
sdk install java 17.0.13-amzn

# use it
sdk use java 17.0.13-amzn
```

run it

```sh
cd freemind
ant run
```
