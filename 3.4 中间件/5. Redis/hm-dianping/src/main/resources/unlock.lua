-- 锁的key
--local key = "lock:order:5" 但是不能写死
local key = KEYS[1]

-- 当前线程标识
--local threadId = "UUID-10086" 但是不能写死
local key =ARGV[1]

-- 获取锁中的线程标识，也就是一个get命令
local id = redis.call('get',key)

-- 比较线程标识与锁中的标识是否一致
if(id == threadId) then
    -- 释放锁 del key，删除成功return 1
    return redis.call('del',key)
end
-- if不成立 return 0
return 0