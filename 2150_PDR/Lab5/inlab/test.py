import string

length = 4;
for abc in range(1, 24):
    letter = string.ascii_lowercase[-abc]
    for i in range(1, 24):
        word = length*letter + string.ascii_lowercase[-i]
        print word,

