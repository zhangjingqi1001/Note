-- 1. 参数列表
-- 1.1 优惠券id
--   需要去redis中读取库存数量，判断是否充足，其中key的前缀seckill:stock是固定的，后面的id是需要传入的
local voucherId = ARGV[1]

-- 1.2 用户id
--   需要知道用户id，才能判断用户之前是否下过单
local userId = ARGV[2]
-- TODO 1.3 订单id
local orderId = ARGV[3]

-- 2.数据相关key
-- 2.1 库存key, lua中是用 .. 拼接字符串
local stockKey = 'seckill:stock:' .. voucherId
-- 2.2 订单key，值是一个set集合，集合名称就是下面。内容是购买订单的用户的id，这样可以记录谁购买了谁没有购买
local orderKey = 'seckill:order:' .. voucherId

-- 3.lua脚本业务
-- 3.1 判断库存是否充足
--  redis.call('get',stockKey)得到的结果是字符串，是无法和数字比较的
if (tonumber(redis.call('get',stockKey))<=0) then
-- 3.2库存不足
    return 1
end
-- 3.2判断用户是否下单
--   借助命令SISMEMBER命令，判断一个给定的值是不是当前set集合中的一个成员，如果存在返回1，不存在返回0
if (redis.call('SISMEMBER',orderKey,userId) == 1) then
-- 3.3redis中存在，说明是重复下单
    return 2
end

-- 3.4 扣库存 incrby stockKey -1
redis.call('incrby',stockKey,-1)
-- 3.5 下单，保存用户 sadd orderKey userId
redis.call('sadd',orderKey,userId)
-- TODO 3.6 发送消息到队列当中，XADD stream.orders * k1 v1 k2 v2.....
redis.call("xadd","stream.orders","*","userId",userId,"voucherId",voucherId,"id",orderId)
return 0

