#!/bin/bash
name="test-cluster"
mkdir $name
for i in 7000 7001 7002
do
	mkdir $name/$i
	touch $name/$i/$i.conf
cat<<EOF > ./$name/$i/$i.conf
port $i
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
EOF
done
