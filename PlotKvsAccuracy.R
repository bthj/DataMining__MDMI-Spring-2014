setwd("/Users/bthj/Documents/ITU/DataMining/IndividualAssignment/plots")

# k_vs_accuracy <- read.table("kVSaccuracy__all_attributes.tab")
# k_vs_accuracy <- read.table("kVSaccuracy__color_attribute.tab")
# k_vs_accuracy <- read.table("kVSaccuracy__age_attribute.tab")
# k_vs_accuracy <- read.table("kVSaccuracy__operating_system_attribute.tab")
k_vs_accuracy <- read.table("./data/kVSaccuracy__age_programmingSkill_yearsAtUni_attribute.tab")

k = k_vs_accuracy$V1
accuracy = k_vs_accuracy$V2

plot( k, accuracy, type='l' )