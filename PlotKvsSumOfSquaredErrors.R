setwd("/Users/bthj/Documents/ITU/DataMining/IndividualAssignment/plots")

# k_vs_sumofsquarederrors <- read.table("kVSsumOfSquaredErrors__k_2-30.tab")
# k_vs_sumofsquarederrors <- read.table("./data/kVSsumOfSquaredErrors__k_2-10.tab")

# k_vs_sumofsquarederrors <- read.table("./data/FlickrPhotos__k-means_sumOfSquaredErrors/FlickrPhotos__kVSsumOfSquaredErrors__k_2-60.tab")
k_vs_sumofsquarederrors <- read.table("./data/FlickrPhotos__k-means_sumOfSquaredError_before_means_computation_fix/FlickrPhotos__kVSsumOfSquaredErrors__k_5-10__before_fix.tab")

k = k_vs_sumofsquarederrors$V1
sum_of_squared_errors = k_vs_sumofsquarederrors$V2

plot( k, sum_of_squared_errors, type='l' )