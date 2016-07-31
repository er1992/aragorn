## Unleashed 2016 ##

library(dplyr)
library(ggplot2)
library(stringr)
library(lubridate)

validations <- read.csv("~/Developer/Data Analysis for Work/Unleashed 2016/src/data_analysis/validations.csv")
metro_trips <- read.csv("~/Developer/Data Analysis for Work/Unleashed 2016/src/data_analysis/metro_trips.CSV")

val_data <- validations
# trip_data <- metro_trips

# trip_data$WeekBeginning <- ymd(as.Date(trip_data$WeekBeginning))

val_data$VALIDATION_DATE <- dmy(val_data$VALIDATION_DATE)

val_data <- val_data[val_data$GTFS_ID == 6665,]

vals_per_wkday <- val_data %>% 
  mutate(wkday = wday(VALIDATION_DATE, label = T)) %>%
  mutate(week = week(VALIDATION_DATE)) %>%
  group_by(week, wkday) %>%
  mutate(num_vals = sum(BAND_BOARDINGS_FLOOR)) %>%
  group_by(wkday) %>%
  mutate(mean_vals = mean(num_vals)) %>%
  distinct()

# how many people go into the city per day on average in 2015?


