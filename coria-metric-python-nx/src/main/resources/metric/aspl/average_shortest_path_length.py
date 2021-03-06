import optparse
from time import gmtime, strftime

import networkx as nx


graph = nx.Graph()


def log(*msg):
    print(strftime("%Y-%m-%d %H:%M:%S", gmtime()), *msg)


def read():
    for line in data_file:
        parse_line(line)

    log("import finished")
    log('Network X graph has the following number of nodes', graph.number_of_nodes())
    log('Network X graph has the following number of edges',graph.number_of_edges())

    log("computing average shortest path length")
    #this will blow up your computers memory
    #cnetr = nx.shortest_path_length(graph)
    calculated_values = ""
    for node in graph.nodes():
        all_shortest_paths_for_node = nx.shortest_path_length(graph, source=node)
        sum_of_lengths = 0
        for target in all_shortest_paths_for_node:
            sum_of_lengths += all_shortest_paths_for_node[target]

        calculated_values = calculated_values + node + "," + str(float(sum_of_lengths)/len(all_shortest_paths_for_node)) + "\n"

    log("finished computing")


    outFile = open(stamp, "w")
    outFile.write(calculated_values)

    log("finished writing to file")
    log("finished average shortest path length execution")

def parse_line(line):
    fields = line.strip().split("\t")
    from_node = fields[1]
    to_node = fields[2]

    if (from_node != to_node):
        graph.add_edge(from_node, to_node)

log("starting import of data...")
parser = optparse.OptionParser()
parser.add_option("-f", "--filename", action="store", dest="filename", default="", help="the name of the file containing tha ASLinks")
parser.add_option("-s", "--stamp", action="store", dest="stamp", default="stamp", help="a hash to map request to response")

options, args = parser.parse_args()
stamp = options.stamp
data_file = open(options.filename)
read()