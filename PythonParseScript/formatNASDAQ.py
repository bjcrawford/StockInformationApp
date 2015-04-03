with open("NASDAQ.txt") as input:
    with open("NASDAQ-Formatted.txt", "w") as output:
        for line in input:
            lineData = line.split("\t")
            strFill = ""
            for i in range(len(lineData[0]), 5):
                strFill += " "
            output.write("<item>" + lineData[0] + strFill + " " + lineData[1].rstrip() + "</item>\n")