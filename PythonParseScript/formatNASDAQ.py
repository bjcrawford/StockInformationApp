with open("NASDAQ.txt") as input:
    with open("NASDAQ-Formatted.txt", "w") as output:
        for line in input:
            lineData = line.split("\t")
            output.write("<item>[" + lineData[0] + "] " + lineData[1].rstrip() + "</item>\n")