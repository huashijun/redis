package com.huashijun.redis.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author huashijun
 */
public class RedisUtils {

    private RedisTemplate<String, Object> redisTemplate;
    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 模糊查询获取key值
     * @param pattern 键配置模式
     * @return 键数量
     */
    public Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(毫秒)
     * @return true-成功 false-失败
     */
    public Boolean expire(String key,Long time){
        return redisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.MILLISECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true-存在 false-不存在
     */
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除单个缓存
     * @param key 单个缓存的key
     */
    public Boolean deleteKey(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个缓存
     * @param keys 多个缓存的key
     */
    public Long deleteMultiKeys(List<String> keys){
        return redisTemplate.delete(keys);
    }

    //============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     */
    public void set(String key,Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(毫秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key,Object value,Long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几
     */
    public Long increment(String key, Long delta){
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几
     */
    public Long decrease(String key, Long delta){
        return redisTemplate.opsForValue().increment(key, delta);
    }

    //================================Map=================================
    /**
     * 获取某个hash Key的某个键对应的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 某个hash Key的某个键对应的值
     */
    public Object hashGet(String key,String item){
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取某个hash Key的所有键值对
     * @param key 键
     * @return 某个hash Key的所有键值对
     */
    public Map<Object,Object> hashEntries(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 存储某个hash Key对应的键值对
     * @param key 键
     * @param map Key对应的键值对
     */
    public void hashPutAll(String key, Map<String,Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 存储某个hash Key对应的键值对,并设置过期时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(毫秒)
     * @return true-成功 false-失败
     */
    public boolean hashPutAll(String key, Map<String,Object> map, Long time){
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     */
    public void hashPut(String key,String item,Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(毫秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true-成功 false-失败
     */
    public boolean hashPut(String key,String item,Object value,Long time) {
        redisTemplate.opsForHash().put(key, item, value);
        return expire(key, time);
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hashDelete(String key, List<String> item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true-存在 false-不存在
     */
    public boolean hashHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param delta 要增加几
     * @return 递增后的值
     */
    public Double hashIncrement(String key, String item,Double delta){
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param delta 要减少几
     * @return 递减后的值
     */
    public Double hashDecrease(String key, String item,Double delta){
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return Set中的所有值
     */
    public Set<Object> setMembers(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true-存在 false-不存在
     */
    public Boolean setIsMember(String key,Object value){
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setAdd(String key, List<Object> values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 将set数据放入缓存并设置过期时间
     * @param key 键
     * @param time 时间(毫秒)
     * @param values 值 可以是多个
     * @return true-成功 false-失败
     */
    public Boolean setAdd(String key,List<Object> values, Long time) {
        Long count = redisTemplate.opsForSet().add(key, values);
        if(count == null || count <= 0) {
            return false;
        }else{
            return expire(key, time);
        }
    }

    /**
     * 获取set缓存的数量
     * @param key 键
     * @return set缓存的数量
     */
    public Long setSize(String key){
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, List<Object> values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取第一个set和第二个set的差集
     * @param firstKey 第一个键
     * @param secondKey 第二个键
     * @return 第一个set和第二个set的差集
     */
    public Set<Object> difference(String firstKey,String secondKey){
        return redisTemplate.opsForSet().difference(firstKey,secondKey);
    }

    /**
     * 获取第一个set和第二个set的交集
     * @param firstKey 第一个键
     * @param secondKey 第二个键
     * @return 第一个set和第二个set的交集
     */
    public Set<Object> intersect(String firstKey,String secondKey){
        return redisTemplate.opsForSet().intersect(firstKey,secondKey);
    }

    /**
     * 获取第一个set和第二个set的并集
     * @param firstKey 第一个键
     * @param secondKey 第二个键
     * @return 第一个set和第二个set的并集
     */
    public Set<Object> union(String firstKey,String secondKey){
        return redisTemplate.opsForSet().union(firstKey,secondKey);
    }

    //===============================ZSet================================

    /**
     * 在 ZSet中插入一条数据
     * @param key   键
     * @param value 要插入的值
     * @param score 设置分数
     * @return true-成功 false-失败
     */
    public Boolean zSetAdd(String key, Object value, Long score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取分数在startScore和endScore之间的值
     * @param key 键
     * @param startScore 起始分数
     * @param endScore 终止分数
     * @return 范围内所有值
     */
    public Set<Object> zSetRange(String key, Long startScore, Long endScore) {
        return redisTemplate.opsForZSet().range(key, startScore, endScore);
    }

    /**
     * 根据value删除，并返回删除个数
     * @param key   键
     * @param value 要删除的值，可传入多个
     * @return 删除个数
     */
    public Long zSetRemove(String key, List<Object> value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 根据下标范围删除，并返回删除个数
     * @param key  键
     * @param startIndex 起始下标
     * @param endIndex 结束下标
     * @return 删除个数
     */
    public Long zSetRemoveRange(String key, Long startIndex, Long endIndex) {
        return redisTemplate.opsForZSet().removeRange(key, startIndex, endIndex);
    }

    /**
     * 删除分数区间内元素，并返回删除个数
     *
     * @param key    键
     * @param startScore 起始分数
     * @param endScore 终止分数
     * @return 删除个数
     */
    public Long zSetDeleteByScore(String key, Double startScore, Double endScore) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, startScore, endScore);
    }

    //===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return list缓存的内容
     */
    public List<Object> listRange(String key, Long start, Long end){
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的数量
     * @param key 键
     * @return list缓存的数量
     */
    public Long listSize(String key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return list中的值
     */
    public Object listIndex(String key,Long index){
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 将list放入缓存的头部
     * @param key 键
     * @param value 值
     * @return 添加的数量
     */
    public Long listLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将list放入缓存的头部,并设置过期时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return true-成功 false-失败
     */
    public boolean listLeftPush(String key, Object value, Long time) {
        Long count = redisTemplate.opsForList().leftPush(key, value);
        if (count == null || count <= 0) {
            return false;
        }else{
            return expire(key, time);
        }
    }

    /**
     * 将list放入缓存的尾部
     * @param key 键
     * @param value 值
     * @return 添加的数量
     */
    public Long listRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将list放入缓存的尾部,并设置过期时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return true-成功 false-失败
     */
    public boolean listRightPush(String key, Object value, Long time) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        if (count == null || count <= 0) {
            return false;
        }else{
            return expire(key, time);
        }
    }

    /**
     * 将多个list放入缓存头部
     * @param key 键
     * @param value 值
     * @return 成功个数
     */
    public Long listLeftPushAll(String key, List<Object> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 将多个list放入缓存头部并设置过期时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return true-成功 false-失败
     */
    public boolean listLeftPushAll(String key, List<Object> value, Long time) {
        Long count = redisTemplate.opsForList().leftPushAll(key, value);
        if (count == null || count <= 0) {
            return false;
        }else{
            return expire(key,time);
        }
    }

    /**
     * 将多个list放入缓存尾部
     * @param key 键
     * @param value 值
     * @return 成功个数
     */
    public Long listRightPushAll(String key, List<Object> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 将多个list放入缓存尾部并设置过期时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return true-成功 false-失败
     */
    public boolean listRightPushAll(String key, List<Object> value, Long time) {
        Long count = redisTemplate.opsForList().rightPushAll(key, value);
        if (count == null || count <= 0) {
            return false;
        }else{
            return expire(key,time);
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public void listIndexUpdate(String key, Long index,Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key,Long count,Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 使用Redis的消息队列
     * @param channel 通道
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message){
        redisTemplate.convertAndSend(channel,message);
    }
}