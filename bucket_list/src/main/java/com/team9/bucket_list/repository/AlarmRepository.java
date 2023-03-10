package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findAllByMember_IdAndReadStatusOrderByIdDesc(Long memberId,byte readStatus, Pageable pageable);

    Page<Alarm> findAllByMember_IdAndReadStatusAndIdGreaterThanOrderByIdDesc(Pageable pageable, Long memberId, byte readStatus, Long id);

    Page<Alarm> findAllByMember_IdAndReadStatusAndIdLessThanOrderByIdDesc(Pageable pageable, Long memberId, byte readStatus, Long id);

    int countByMember_Id(Long memberId);

    Optional<Alarm> findBySenderNameAndMemberIdAndCategory(String senderName, Long memberId, byte category);
    Optional<Alarm> findByMemberIdAndPostIdAndCategory(Long memberId, Long postId, byte category);

    @Modifying(clearAutomatically = true)
    @Query(value = "update alarm a set a.read_status = 1 where a.member_id = :memberId and a.read_status = 0 and a.category not between 4 and 5;",nativeQuery = true)
    int readAllAlarm(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update alarm a set a.read_status = 1 where a.member_id = :memberId and a.alarm_id = :id and a.read_status = 0;",nativeQuery = true)
    int readAlarm(@Param("memberId") Long memberId, @Param("id") Long id);

    int deleteAlarmByCategoryAndPostIdAndSenderName(byte category, Long postId, String senderName);
}
