setwd("/Users/bthj/Documents/ITU/DataMining/IndividualAssignment/plots")

# k_vs_sumofsquarederrors <- read.table("kVSsumOfSquaredErrors__k_2-30.tab")
k_vs_sumofsquarederrors <- read.table("./data/kVSsumOfSquaredErrors__k_2-10.tab")

k = k_vs_sumofsquarederrors$V1
sum_of_squared_errors = k_vs_sumofsquarederrors$V2

plot( k, sum_of_squared_errors, type='l' )