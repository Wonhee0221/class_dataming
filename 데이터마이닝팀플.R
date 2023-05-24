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

#박스플롯으로 이상치 확인ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
boxplot(used_car$연식)$stats #상자그림 통계치 출력
boxplot(used_car$주행거리)$stats
boxplot(used_car$가격)$stats
boxplot(used_car$신차대비가격)$stats
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

#이상치제거코드 아직 안에 변수이름이랑 값 안바꿈
mpg$hwy <-ifelse(mpg$hwy < 12 | mpg$hwy > 37,NA,mpg$hwy)
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

str(used_car)

#write.csv(cars_df, file = "cars_processed2.csv")

table(used_car$옵션_선루프)
dim(used_car)
summary(used_car)



pre_car <- used_car
#범주형 데이터 벡터 변환
factorVar <- c("이름","연료","연식","배기량","색상","옵션_선루프","옵션_파노라마선루프","옵션_열선앞","옵션_열선뒤","옵션_전방센서","옵션_후방센서","옵션_전방캠","옵션_후방캠","옵션_어라운드뷰","옵션_네비순정","소유자변경횟수","사고상세_전손","보험_내차피해.횟수.","사고상세_타차가해.횟수.","신차대비가격")
pre_car[,factorVar] = lapply(pre_car[,factorVar],factor)

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



#선형회귀 값차이본것.
target <- test_y
target
result <- predict(tmp3, newdata = test_x)
result <- cbind(test_y, result)
resultfinal <- result[,c("가격", "result")]
resultfinal
resultfinal$차이값 <- abs((resultfinal$result - resultfinal$가격) /resultfinal$가격) * 100
resultfinal
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


#다양한 선택법들 (우리가했을땐 전진이 젤 작게 나오고 나머지 두개는 값이 같음)
#전진선택법
forward<-step(tmp3,direction = "forward")
summary(forward)
#후진선택법
backward<-step(tmp3,direction = "backward")
summary(backward)
#단계적선택법
stepwise<-step(tmp3,direction = "both")
summary(stepwise)
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

#이게 뭐 스케일 코드이긴한데 아직 아무것도 아님.
str(pre_car)
scale_car <- preProcess(x=pre_car,method = c("center","scale"))
scale_car
str(scale_car)


rf_model <- randomForest(가격 ~ ., data = train)
rf_predictions <- predict(rf_model, newdata = test)
lr_model <- glmnet(x = as.matrix(train_x),
                   y = train_y,
                   family = "gaussian",
                   alpha = 1)

#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ




#랜덤포레스트 결과
rf_predictions <- predict(rf_model, newdata = test_x)

rf_predictions <- cbind(test_y, rf_predictions)
resultfinal <- rf_predictions[,c("가격", "rf_predictions")]
resultfinal
resultfinal$차이값 <- abs((resultfinal$rf_predictions - resultfinal$가격) /resultfinal$가격) * 100
summary(resultfinal)

#값측정
ssr <- sum((resultfinal$rf_predictions - mean(resultfinal$가격))**2)
sst <- sum((resultfinal$가격 - mean(resultfinal$가격))**2)
r_squared <- ssr / sst
r_squared

#이건 보류 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
ctrl <- trainControl(method = "repeatedcv",repeats = 5)
rffit <- train(가격~.,
               data = train[,-1],
               method= "rf",
               trControl =ctrl,
               preProcess=c("center","scale"),
               metric = "Rsquared")
rffit
plot(rffit)
pred_test <- predict(rffit, test_x)
pred_test
confusionMatrix(pred_test,test$가격)

importnace_rf <- varImp(rffit, scale =FALSE )
plot(importnace_rf)
summary(importnace_rf)
#ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ





#지피티가 랜덤포레스트랑 선형회귀 분석 섞은거 코드 (이것도 아직 뭐 없음)
library(randomForest)
library(glmnet)

# 데이터 준비
data <- read.csv("./Data/cars_processed.csv")

# 훈련 데이터와 테스트 데이터로 분할
#train_indices <- sample(1:nrow(data), nrow(data) * 0.8)
#train_data <- data[train_indices, ]
#test_data <- data[-train_indices, ]

newdata <- sort(sample(nrow(newdata),nrow(newdata)*0.75))
train <- newdata_d[newdata,]
test <- newdata_d[-newdata,]

# 첫 번째 단계 모델: 랜덤 포레스트
rf_model <- randomForest(가격 ~ ., data = train)
summary(rf_model)
# 두 번째 단계 모델: 선형 회귀 모델
lr_model <- glmnet(x = as.matrix(train[, -30]),
                   y = train$가격,
                   family = "gaussian",
                   alpha = 1)

# 테스트 데이터에 대한 첫 번째 단계 모델의 예측
rf_pred <- predict(rf_model, newdata = test_x)
summary(rf_pred)

# 테스트 데이터에 대한 두 번째 단계 모델의 예측
lr_predictions <- predict(lr_model, newx = as.matrix(test_data[, -target_variable_column_index]))

# 예측 결과를 합친 후 최종 예측값 계산
stacked_predictions <- (rf_predictions + lr_predictions) / 2

# 최종 예측값 평가
accuracy <- your_evaluation_function(stacked_predictions, test_data$target_variable)

