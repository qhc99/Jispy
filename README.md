Java implementation of lis.py from https://norvig.com/lispy.html and http://norvig.com/lispy2.html.

See file "Jispy.java" for usage of interpreter API. See tests file and "*.ss" files in folder "src/main/resources" for supported lisp features.

The original implementation contains bug because it misunderstands the meaning of ordered pair. You will find it once you build a tree instead of list using car and cdr.