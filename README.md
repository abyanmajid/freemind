freemind but it actually builds on mac ;p

you'll need java 17 and apache ant.

you might also need to install some missing macos dependencies. just ask your coding agent to brew it.

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
