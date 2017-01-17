import numpy as np

class single:
    '''
        感知器模型
    '''
    def __init__(self,dimen):
        self.weight = np.zeros(dimen)
        self.bias = 0
        self.ratio = 0.1

    def sigmoid(self,inputVec):
        '''
        阈值函数
        :param inputVec:
        :return:
        '''
        return 1/(1 + np.exp(- self.out(inputSet)))

    def out(self,inputVec):
        '''
        输出，并未加阈值函数
        :param inputVec:
        :return:
        '''
        return sum(self.weight * inputVec) + self.bias

    def train(self,inputSet,outputSet,times = 100):
        for x in range(0,times):
            for i in range(0,len(inputSet)):
                out = self.out(inputSet[i])
                delta = outputSet[i] - out
                self.weight += self.ratio * delta * inputSet[i]
                self.bias += self.ratio * delta

if __name__ == '__main__':
    single = single(2)

    inputSet = np.array([1,1,2,2]).reshape(2,-1)
    outputSet = np.array([1,2])

    single.train(inputSet,outputSet)

    print(single.out([1,1]))

    print(single.__doc__)