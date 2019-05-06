#!/bin/bash
name="test-load-balancer"
mkdir $name
cd $name
for i in 8000 8100
do
mkdir $i
cat<<EOF > ./$i/index.html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Port $i</title>
</head>
<body>
    <h1>Hello world $i</h1>
</body>
</html>
EOF
done