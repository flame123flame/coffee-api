package coffee.web.service;

import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import coffee.buy.vo.res.Installment;
import coffee.model.TimeSell;
import framework.constant.LottoConstant;
import framework.utils.ConvertDateUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WebTimeSellService {

	public List<Installment> setDateMonthly(List<TimeSell> req) {
		Calendar thisDateCal = Calendar.getInstance();
		if (req.size() <= 0) {
			return new ArrayList<Installment>();
		}
		List<Installment> listTimeSet = new ArrayList<Installment>();
		String statusDate = null;

		for (TimeSell item : req) {

			Calendar end = Calendar.getInstance();
			end.setTime(item.getTimeClose());

			if (setDate(item.getTimeClose(), 0).after(setDate(item.getTimeOpen(), 0))) {
				if (thisDateCal.get(Calendar.DAY_OF_MONTH) >= end.get(Calendar.DAY_OF_MONTH)) {
					statusDate = "ปกติ-ใหม่";
					// break;
				} else {
					statusDate = "ปกติ-เดิม";
				}
			} else {
				// ่คล่อมเดือน
				if (thisDateCal.get(Calendar.DAY_OF_MONTH) <= end.get(Calendar.DAY_OF_MONTH)) {
					statusDate = "คล่อมเดือน-ใหม่";
					break;
				} else {
					statusDate = "คล่อมเดือน-เดิม";
				}
			}
		}
		System.out.println(statusDate);
		// มีคล่อมเดือน และเดือนนีิ้เป็นเดือนใหม่ของวันที่คล่อม
		if ("คล่อมเดือน-ใหม่".equals(statusDate)) {
			req.forEach((item) -> {
				Installment timeSell = new Installment();

				if (setDate(item.getTimeClose(), 0).before(setDate(item.getTimeOpen(), 0))) {
					Calendar end = Calendar.getInstance();
					end.setTime(item.getTimeClose());
					if (thisDateCal.get(Calendar.DAY_OF_MONTH) <= end.get(Calendar.DAY_OF_MONTH)) {
						item.setTimeOpen(setDate(item.getTimeOpen(), -1));
						item.setTimeClose(setDate(item.getTimeClose(), 0));
					}
				} else {
					item.setTimeOpen(setDate(item.getTimeOpen(), -1));
					item.setTimeClose(setDate(item.getTimeClose(), -1));
				}

				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		} else if ("คล่อมเดือน-เดิม".equals(statusDate)) {
			/**
			 * มีคล่อมเดือน แต่เป็นเดือนนี้อยู่
			 */
			req.forEach((item) -> {
				Installment timeSell = new Installment();

				if (setDate(item.getTimeClose(), 0).before(setDate(item.getTimeOpen(), 0))) {
					item.setTimeOpen(setDate(item.getTimeOpen(), 0));
					item.setTimeClose(setDate(item.getTimeClose(), +1));
				} else {
					item.setTimeOpen(setDate(item.getTimeOpen(), 0));
					item.setTimeClose(setDate(item.getTimeClose(), 0));
				}

				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		} else if ("ปกติ-ใหม่".equals(statusDate)) {
			req.forEach((item) -> {
				Installment timeSell = new Installment();
				item.setTimeOpen(setDate(item.getTimeOpen(), +1));
				item.setTimeClose(setDate(item.getTimeClose(), +1));
				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		} else {
			req.forEach((item) -> {
				Installment timeSell = new Installment();

				item.setTimeOpen(setDate(item.getTimeOpen(), 0));
				item.setTimeClose(setDate(item.getTimeClose(), 0));

				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		}
		return listTimeSet;
	}

	public List<Installment> setDateMonthlyAfter(List<TimeSell> req) {
		Calendar thisDateCal = Calendar.getInstance();
		if (req.size() <= 0) {
			return new ArrayList<Installment>();
		}
		List<Installment> listTimeSet = new ArrayList<Installment>();
		String statusDate = null;

		for (TimeSell item : req) {

			Calendar end = Calendar.getInstance();
			end.setTime(item.getTimeClose());

			if (setDateAfter(item.getTimeClose(), 0).after(setDateAfter(item.getTimeOpen(), 0))) {
				if (thisDateCal.get(Calendar.DAY_OF_MONTH) <= end.get(Calendar.DAY_OF_MONTH)) {
					statusDate = "ปกติ-ใหม่";
					// break;
				} else {
					statusDate = "ปกติ-เดิม";
				}
			} else {
				// ่คล่อมเดือน
				if (thisDateCal.get(Calendar.DAY_OF_MONTH) <= end.get(Calendar.DAY_OF_MONTH)) {
					statusDate = "คล่อมเดือน-ใหม่";
					break;
				} else {
					statusDate = "คล่อมเดือน-เดิม";
				}
			}
		}
		System.out.println(statusDate);
		// มีคล่อมเดือน และเดือนนีิ้เป็นเดือนใหม่ของวันที่คล่อม
		if ("คล่อมเดือน-ใหม่".equals(statusDate)) {
			req.forEach((item) -> {
				Installment timeSell = new Installment();

				if (setDateAfter(item.getTimeClose(), 0).before(setDateAfter(item.getTimeOpen(), 0))) {
					Calendar end = Calendar.getInstance();
					end.setTime(item.getTimeClose());
					if (thisDateCal.get(Calendar.DAY_OF_MONTH) <= end.get(Calendar.DAY_OF_MONTH)) {
						item.setTimeOpen(setDateAfter(item.getTimeOpen(), -1));
						item.setTimeClose(setDateAfter(item.getTimeClose(), 0));
					}
				} else {
					item.setTimeOpen(setDateAfter(item.getTimeOpen(), -1));
					item.setTimeClose(setDateAfter(item.getTimeClose(), -1));
				}

				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		} else if ("คล่อมเดือน-เดิม".equals(statusDate)) {
			/**
			 * มีคล่อมเดือน แต่เป็นเดือนนี้อยู่
			 */
			req.forEach((item) -> {
				Installment timeSell = new Installment();

				if (setDateAfter(item.getTimeClose(), 0).before(setDateAfter(item.getTimeOpen(), 0))) {
					item.setTimeOpen(setDateAfter(item.getTimeOpen(), 0));
					item.setTimeClose(setDateAfter(item.getTimeClose(), +1));
				} else {
					item.setTimeOpen(setDateAfter(item.getTimeOpen(), 0));
					item.setTimeClose(setDateAfter(item.getTimeClose(), 0));
				}

				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		} else {
			req.forEach((item) -> {
				Installment timeSell = new Installment();

				item.setTimeOpen(setDateAfter(item.getTimeOpen(), 0));
				item.setTimeClose(setDateAfter(item.getTimeClose(), 0));

				timeSell.setTimeOpen(item.getTimeOpen());
				timeSell.setTimeClose(item.getTimeClose());
				timeSell.setTimeOpenStr(ConvertDateUtils.formatDateToString(item.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSell.setTimeCloseStr(ConvertDateUtils.formatDateToString(item.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSell);
			});
		}
		return listTimeSet;
	}

	private Date setDate(Date req, int addMonth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(req);
		cal.set(Calendar.YEAR, Year.now().getValue());
		cal.set(Calendar.MONTH, MonthDay.now().getMonthValue() - 1);
		cal.add(Calendar.MONTH, addMonth);
		return cal.getTime();
	}

	private Date setDateAfter(Date req, int addMonth) {
		Calendar thisCal = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(req);
		cal.set(Calendar.YEAR, Year.now().getValue());
		cal.set(Calendar.MONTH, MonthDay.now().getMonthValue());
		cal.set(Calendar.DAY_OF_MONTH, thisCal.get(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public Date setDateTime(Date req) {
		Calendar thisCal = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(req);
		cal.set(Calendar.YEAR, Year.now().getValue());
		cal.set(Calendar.MONTH, MonthDay.now().getMonthValue() - 1);
		cal.set(Calendar.DAY_OF_MONTH, thisCal.get(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public Date setDateTime(Date req, int dayAdd) {
		Calendar thisCal = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(req);
		cal.set(Calendar.YEAR, Year.now().getValue());
		cal.set(Calendar.MONTH, MonthDay.now().getMonthValue() - 1);
		cal.set(Calendar.DAY_OF_MONTH, thisCal.get(Calendar.DAY_OF_MONTH));
		cal.add(Calendar.DAY_OF_MONTH, dayAdd);
		return cal.getTime();
	}

	public Date subtractDay(Date req, int dayAdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(req);
		cal.add(Calendar.DAY_OF_MONTH, dayAdd);
		return cal.getTime();
	}

	public Date setDateTimeAfter(Date req) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(req);
		cal.set(Calendar.YEAR, Year.now().getValue());
		cal.set(Calendar.MONTH, MonthDay.now().getMonthValue() - 1);
		cal.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
		cal.add(Calendar.DATE, 1);

		return cal.getTime();
	}

	public List<Installment> getListInstallmentByType(String typeInstallment, List<TimeSell> timeSellList,
			Boolean ignoreWeekly) {
		List<Installment> dataRes = new ArrayList<Installment>();
		if (LottoConstant.TYPE_INSTALLMENT.MONTHLY.equals(typeInstallment)) {
			dataRes.clear();
			dataRes.addAll(setDateMonthly(timeSellList));
		} else if (LottoConstant.TYPE_INSTALLMENT.DAILY.equals(typeInstallment)) {
			Calendar thisDateCal = Calendar.getInstance();
			int count = 1;
			int lengthTimeSell = timeSellList.size();
			for (TimeSell item : timeSellList) {

				Calendar end = Calendar.getInstance();
				end.setTime(item.getTimeClose());

				Calendar start = Calendar.getInstance();
				start.setTime(item.getTimeOpen());

				String statusDate = "";
				int dayStartAdd = 0;
				int dayEndAdd = 0;

				if (getHourMin(thisDateCal.get(Calendar.HOUR_OF_DAY), thisDateCal.get(Calendar.MINUTE)) <= getHourMin(
						end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE))) {
					if (getHourMin(thisDateCal.get(Calendar.HOUR_OF_DAY),
							thisDateCal.get(Calendar.MINUTE)) <= getHourMin(start.get(Calendar.HOUR_OF_DAY),
									start.get(Calendar.MINUTE))
							&& getHourMin(end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE)) < getHourMin(
									start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE))) {
						// current < end | current < start
						statusDate = "คล่อม-งวดเดิม-เปิด";
						dayStartAdd = -1;
						dayEndAdd = 0;
					} else if (getHourMin(thisDateCal.get(Calendar.HOUR_OF_DAY),
							thisDateCal.get(Calendar.MINUTE)) <= getHourMin(start.get(Calendar.HOUR_OF_DAY),
									start.get(Calendar.MINUTE))) {
						// current < end | current > start
						statusDate = "ปกติ-งวดเดิม-เปิด";
						dayStartAdd = 0;
						dayEndAdd = 0;
					} else {
						// current > end | current > start
						statusDate = "ปกติ-งวดถัดไป-ปิด";
						dayEndAdd = 0;
						dayStartAdd = 0;
					}
				} else if (getHourMin(thisDateCal.get(Calendar.HOUR_OF_DAY), thisDateCal
						.get(Calendar.MINUTE)) > getHourMin(end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE))) {
					if (getHourMin(thisDateCal.get(Calendar.HOUR_OF_DAY),
							thisDateCal.get(Calendar.MINUTE)) <= getHourMin(start.get(Calendar.HOUR_OF_DAY),
									start.get(Calendar.MINUTE))) {
						// current > end | current < start
						statusDate = "คล่อม-งวดถัดไป-ปิด";
						dayStartAdd = 0;
						dayEndAdd = 1;
					} else {
						// current > end | current > start
						statusDate = "ปกติ-งวดถัดไป-ปิด";
						dayEndAdd = 1;
						dayStartAdd = 1;
					}
				}
				// }

				Calendar closeCalendar = Calendar.getInstance();
				closeCalendar.setTime(setDateTime(item.getTimeClose()));
				Installment timeSet = new Installment();
				timeSet.setLengthTimeSell(lengthTimeSell);
				timeSet.setCount(count);
				// ignoreWeekly = ignoreWeekly;
				if (ignoreWeekly) {
					int dayOfWeek = thisDateCal.get(Calendar.DAY_OF_WEEK);
					int dayAdd = 0;
					if (dayOfWeek == 1) {
						dayAdd = 1;
					} else if (dayOfWeek == 7) {
						dayAdd = 2;
					} else if (thisDateCal.after(closeCalendar)) {
						dayAdd = 1;
					}
					timeSet.setTimeOpen(setDateTime(item.getTimeOpen(), dayAdd + dayStartAdd));
					timeSet.setTimeClose(setDateTime(item.getTimeClose(), dayAdd + dayEndAdd));
				} else if (thisDateCal.after(closeCalendar)) {
					timeSet.setTimeOpen(setDateTime(item.getTimeOpen(), 0 + dayStartAdd));
					timeSet.setTimeClose(setDateTime(item.getTimeClose(), 0 + dayEndAdd));
				} else {
					timeSet.setTimeOpen(setDateTime(item.getTimeOpen(), 0 + dayStartAdd));
					timeSet.setTimeClose(setDateTime(item.getTimeClose(), 0 + dayEndAdd));
				}

				timeSet.setTimeOpenStr(ConvertDateUtils.formatDateToString(timeSet.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSet.setTimeCloseStr(ConvertDateUtils.formatDateToString(timeSet.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				dataRes.add(timeSet);
				count++;
			}
		} else if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(typeInstallment)) {

			int round = 0;
			int lengthTimeSell = timeSellList.size();
			// เช็ค date วันใหม่ งวดเดิม
			int dayAdd = 0;
			if (timeSellList.size() > 0) {
				TimeSell lastList = timeSellList.get(timeSellList.size() - 1);
				Calendar start = Calendar.getInstance();
				start.setTime(setDateTime(lastList.getTimeOpen()));
				Calendar end = Calendar.getInstance();
				end.setTime(setDateTime(lastList.getTimeClose()));

				Calendar thisDateCal = Calendar.getInstance();

				if (thisDateCal.getTimeInMillis() <= end.getTimeInMillis()
						&& thisDateCal.getTimeInMillis() < start.getTimeInMillis()) {
					dayAdd = -1;
				} else if (thisDateCal.getTimeInMillis() <= end.getTimeInMillis()
						&& thisDateCal.getTimeInMillis() >= start.getTimeInMillis()) {
					dayAdd = 0;
				}
			}
			int newRound = 0;
			for (TimeSell item : timeSellList) {
				Date timeClose = setDateTime(item.getTimeClose(), dayAdd);
				Date timeOpen = setDateTime(item.getTimeOpen(), dayAdd);
				timeSellList.get(round).setTimeOpen(timeOpen);
				/**
				 * เช็คคล่อมวัน วันใหม่ เวลาใหม่
				 */

				Date timeCloseRoundBefore;
				if (round == 0) {
					timeCloseRoundBefore = setDateTime(timeSellList.get(round).getTimeOpen(), dayAdd);
					timeSellList.get(round).setTimeClose(setDateTime(timeSellList.get(round).getTimeClose(), dayAdd));
				} else {
					timeCloseRoundBefore = setDateTime(timeSellList.get(round - 1).getTimeClose(), dayAdd);
				}
				/**
				 * ถ้าเป็นวันใหม่ ให้ + วัน 1;
				 */
				if (newRound != 0) {
					timeCloseRoundBefore = setDateTime(timeSellList.get(newRound).getTimeClose(), dayAdd);
				}
				int closeDayAdd = 0;
				if (timeClose.before(timeOpen)) {
					closeDayAdd = 1;
				}
				if (timeCloseRoundBefore.after(timeClose)) {
					timeClose = setDateTime(timeClose, closeDayAdd);
					Long hour = getGetBetween2Time(timeOpen, timeClose);
					if (hour >= 24) {
						timeClose = subtractDay(timeClose, -1);
					}
					timeSellList.get(round).setTimeClose(timeClose);
					if (round > 0 && newRound == 0) {
						newRound = round - 1;
					}
				}

				round++;
				Installment timeSet = new Installment();
				timeSet.setLengthTimeSell(lengthTimeSell);
				timeSet.setCount(round);
				timeSet.setTimeOpen(timeOpen);
				timeSet.setTimeClose(timeClose);
				timeSet.setTimeOpenStr(ConvertDateUtils.formatDateToString(timeOpen, ConvertDateUtils.DD_MM_YYYY_HHMMSS,
						ConvertDateUtils.LOCAL_EN));
				timeSet.setTimeCloseStr(ConvertDateUtils.formatDateToString(timeClose,
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				dataRes.add(timeSet);
			}
		}
		return dataRes;
	}

	private Long getGetBetween2Time(Date currentDate, Date closeDate) {
		Long diffInMillies = Math.abs(closeDate.getTime() - currentDate.getTime());
		Long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		return diff;
	}

	private int getHourMin(int hour, int min) {
		String hm = StringUtils.leftPad(String.valueOf(hour), 2, "0")
				+ StringUtils.leftPad(String.valueOf(hour), 2, "0");
		return Integer.parseInt(hm);
	}

	public String getInstallmentStr(String dateType, Date dateInstallment, int lengthTimeSell) {
		String installmentStr = "";
		if (LottoConstant.TYPE_INSTALLMENT.MONTHLY.equals(dateType) || lengthTimeSell == 1) {
			installmentStr = ConvertDateUtils.formatDateToString(dateInstallment, ConvertDateUtils.DD_MM_YYYY,
					ConvertDateUtils.LOCAL_EN);
		} else if (LottoConstant.TYPE_INSTALLMENT.DAILY.equals(dateType)) {
			installmentStr = ConvertDateUtils.formatDateToString(dateInstallment, ConvertDateUtils.DD_MM_YYYY_HHMM,
					ConvertDateUtils.LOCAL_EN);
		} else if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(dateType)) {
			installmentStr = ConvertDateUtils.formatDateToString(dateInstallment, ConvertDateUtils.DD_MM_YYYY,
					ConvertDateUtils.LOCAL_EN);
		}
		return installmentStr;
	}
}
