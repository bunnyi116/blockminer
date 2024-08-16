**简体中文**|[English](./README_EN.md)

## 🟢 开发中！

# BlockMiner

使用活塞回收特性将目标方块进行移除

## 方块支持情况

#### 方块白名单（自定义添加支持的方块）

- 基岩

#### 方块黑名单（防误触）

- 无

#### 服务器方块黑名单（无法通过指令更改，内置过滤器）

- 屏障
- 普通型命令方块
- 连锁型命令方块
- 循环型命令方块
- 结构空位
- 结构方块
- 拼图方块

#### 默认过滤器

- 空气
- 可替换方块

## 使用说明

#### 执行条件

使用前需准备好如下物品：

| 条件     |   数量/说明    | 
|--------|:----------:|
| 活塞     |   2（必选）    | 
| 拉杆     |   1（必选）    | 
| 黏液块    |   0（可选）    |  
| 效率5钻石镐 | 推荐(增加破坏速度) | 
| 急迫2信标  | 推荐(增加破坏速度) |  

#### 开启方法1

使用 `空手` 右键 `受支持的目标方块` 开启/关闭此模组（注意要`空手`）

#### 开启方法2

输入 `/blockMiner` 命令开启/关闭此模组

#### 启用后

选中 `受支持的方块`，然后左键点击该方块

如果本模组帮你节省了大量时间的话，请给个 Star 呗 QwQ。

## 命令

| 命令                                              | 说明                              |
|:------------------------------------------------|:--------------------------------|
| /blockMiner                                     | 开启/关闭模组                         |
| /blockMiner debug                               | 开启/关闭调试                         |
| /blockMiner whitelist add <block>               | 添加白名单方块                         |
| /blockMiner whitelist remove <block>            | 移除白名单方块                         |
| /blockMiner blacklist add <block>               | 添加黑名单方块                         |
| /blockMiner blacklist remove <block>            | 移除黑名单方块                         |
| /blockMiner task add clear                      | 清理任务                            |
| /blockMiner task tickMax <1-3>                  | 单TICK同时执行任务的最大值（放置方案有东南西北朝向时为1） |
| /blockMiner task order scheme                   | 方案优先级（基于目标方块向上下东南西北偏移）          |
| /blockMiner task order piston                   | 活塞朝向优先级（活塞放置的面朝方向）              |
| /blockMiner task order lever                    | 拉杆朝向优先级                         |
| /blockMiner task order priorityIsNotPlacedBlock | 优先不放置方块                         |

~~~~
🟢🔴

