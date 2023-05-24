library(caret)
library(foreign)
library(dplyr)
library(ggplot2)
library(readxl)
library(lawstat)
library(randomForest)
library(glmnet)

used_car <- read.csv(file = "./Data/cars_processed.csv", header = TRUE)
#View(used_car)
str(used_car)
summary(used_car)
#필요없는 변수 제거(인덱스, 링크)
used_car <- used_car[,c(-1,-18,-21,-22,-23,-30)] 
str(used_car)

#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

str(used_car)

#write.csv(cars_df, file = "cars_processed2.csv")

table(used_car$옵션_선루프)
dim(used_car)
summary(used_car)



pre_car <- used_car
#범주형 데이터 벡터 변환
factorVar <- c("이름","연료","연식","배기량","색상","옵션_선루프","옵션_파노라마선루프","옵션_열선앞","옵션_열선뒤","옵션_전방센서","옵션_후방센서","옵션_전방캠","옵션_후방캠","옵션_어라운드뷰","옵션_네비순정","소유자변경횟수","사고상세_전손","보험_내차피해.횟수.","사고상세_타차가해.횟수.")
pre_car[,factorVar] <- lapply(pre_car[,factorVar],factor)

set.seed(2020)

library(mltools)
library(data.table)

#더미변수로 바꾸는 코드
used_car$색상<-as.factor(used_car$색상)
used_car$연료<-as.factor(used_car$연료)
newdata<-one_hot(as.data.table(used_car))
summary(newdata)
str(newdata)

#데이터셋 나누기 (트레인 70, 테스트 30)

newdata_d <- newdata
newdata <- sort(sample(nrow(newdata),nrow(newdata)*0.7))
train <- newdata_d[newdata,]
test <- newdata_d[-newdata,]
str(train)
train_x <- train[,-30]
train_y <- train[,30]
str(train_y)
test_x <- test[,-30]
test_y <- test[,30]

str(test_y)

#선형회귀 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
tmp3<-lm(가격~주행거리+연료_0+연료_1+연료_2+ +연식+ 배기량+색상_0+색상_1+색상_2+색상_3+색상_4+색상_5+옵션_선루프+옵션_열선앞+옵션_열선뒤+옵션_전방센서+옵션_후방센서+옵션_전방캠+옵션_후방캠+옵션_어라운드뷰+옵션_네비순정+소유자변경횟수+사고상세_전손+보험_내차피해.횟수.+보험_내차피해.가격.+사고상세_타차가해.횟수.+보험_타차피해.가격.+신차대비가격,data=train)
summary(tmp3)

pre_d <- predict(tmp3, newdata = test_x)
pre_d
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


#이걸 사용 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
ctrl <- trainControl(method = "repeatedcv",repeats = 5)
rffit <- train(가격~.,
               data = train[,-1],
               method= "rf",
               trControl =ctrl,
               preProcess=c("center","scale"),
               metric = "Rsquared")

print(rffit)
plot(rffit)
pred_test <- predict(rffit, test_x)
pred_test

#변수중요도
importnace_rf <- varImp(rffit, scale =FALSE )
plot(importnace_rf)
summary(importnace_rf)


pred_test <- cbind(test_y, pred_test)
resultfinal <- pred_test[,c("가격", "pred_test")]
resultfinal
resultfinal$차이값 <- abs((resultfinal$pred_test - resultfinal$가격) /resultfinal$가격) * 100
resultfinal

resultfinal$가격 <- format(resultfinal$가격, big.mark = ",")
resultfinal$pred_test <- format(resultfinal$pred_test, big.mark = ",")
View(resultfinal)
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
