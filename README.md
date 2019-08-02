[![Build Status](https://travis-ci.org/dice-group/Palmetto.svg?branch=master)](https://travis-ci.org/dice-group/Palmetto)

Palmetto
========
<i><b>P</b>almetto is a qu<b>al</b>ity <b>me</b>asuring <b>t</b>ool for <b>to</b>pics</i>

This is the implementation of coherence calculations for evaluating the quality of topics. If you want to learn more about coherence calculations and their meaning for topic evaluation, take a look at the <a href="http://palmetto.aksw.org/">project homepage</a> - especially at the publications.

<span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Palmetto</span> from <a xmlns:cc="http://creativecommons.org/ns#" href="http://cs.uni-paderborn.de/ds/" property="cc:attributionName" rel="cc:attributionURL">DICE</a> is licensed under a <a rel="license" href="https://www.gnu.org/licenses/agpl.txt">AGPL v3.0 License</a>.

Please take a look at the the wikipage to read <a href="https://github.com/AKSW/Palmetto/wiki/How-Palmetto-can-be-used">how Palmetto can be used</a>.

If you are using Palmetto for an experiment or something similar that leads to a publication, please cite the paper "Exploring the Space of Topic Coherence Measures" that you can find on the project website. A link to the project website is welcome as well :)

### Directories

The `palmetto` directory contains the Palmetto library.

The `webApp` directory contains a web application offering a small demo as well as a web service API for using Palmetto.

### Docker

Palmetto can be used as a docker container. The container can be build and run from the `webApp` directory.

```
docker build -t palmetto .
docker run -p 7777:8080 -d -m 4G -v /path/to/indexes/:/usr/src/indexes/:ro palmetto`
```

After that there is a Tomcat listening on port 7777. The demo application can be accessed using `http://localhost:7777/palmetto-webapp/index.html`.


### Use Palmetto with custom dataset

We can use this tool set to calculate the coherence score on a custom data set instead of wikipedia dataset.
For that we need to create the lucene index ourself based on the coherence score type requirement. 

Different coherence score has different index requirement, read this to get more understanding on the index creation (https://github.com/dice-group/Palmetto/wiki/How-to-create-a-new-index)

#### Build This project

```bash
cd palmetto
mvn clean compile assembly:single

# Once built, the big jar with all dependency is kept under target folder.
# Use all the commands available under this project using bellow command

java -cp target/Palmetto-jar-with-dependencies.jar <full-package-path-of-a-Main-class-file>
```


#### Create an index 

To create reference corpus for the coherence calculation, then you have to
index the document in lucene. The document should be formattted in such a way
that
1. Each line holds one document
2. Document words are separated by space
3. N-gram words are joined with underscore.

```bash
java -cp target/Palmetto-jar-with-dependencies.jar org.aksw.palmetto.LuceneIndexCreater <index-folder-name> <input-text-file>
```

#### Coherence score calculation 

The topic vocabs has to be formated as above mentioned three steps to ensure
they matche the words indexed in lucene.

```bash
java -cp target/Palmetto-jar-with-dependencies.jar org.aksw.palmetto.Palmetto <index-folder-name> <topic-top-n-vocab>
```

The result will be stored under this path, `<index-dir>/coherence_score_<coherence-type>.txt`

Each line in the result file holds the coherence score for the corresponding
topic.

