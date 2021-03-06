package com.dssm.esc.model.analytical.implSevice;

import com.dssm.esc.model.analytical.EmergencyService;
import com.dssm.esc.model.entity.emergency.DrillProcationDetailEntity;
import com.dssm.esc.model.entity.emergency.EmergencyPlanEvaAddEntity;
import com.dssm.esc.model.entity.emergency.GetProjectEveInfoEntity;
import com.dssm.esc.model.entity.emergency.PlanDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanNameSelectEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailEntity;
import com.dssm.esc.model.entity.emergency.PlanStarListDetailObjEntity;
import com.dssm.esc.model.entity.emergency.PlanSuspandEntity;
import com.dssm.esc.model.entity.emergency.SendNoticyEntity;
import com.dssm.esc.model.jsonparser.OnDataCompleterListener;
import com.dssm.esc.model.jsonparser.emergency.AssignParser;
import com.dssm.esc.model.jsonparser.emergency.BeginExecutePlanParser;
import com.dssm.esc.model.jsonparser.emergency.BoHuiEventListParser;
import com.dssm.esc.model.jsonparser.emergency.BusinessTypeParser;
import com.dssm.esc.model.jsonparser.emergency.CheckEmergencySignParser;
import com.dssm.esc.model.jsonparser.emergency.DeleteEventParser;
import com.dssm.esc.model.jsonparser.emergency.DrillPrecautionDetailParser;
import com.dssm.esc.model.jsonparser.emergency.DrillProjectNameParser;
import com.dssm.esc.model.jsonparser.emergency.EmergencyPlanEvaAddParser;
import com.dssm.esc.model.jsonparser.emergency.EventSceneParser;
import com.dssm.esc.model.jsonparser.emergency.GetAuthPlanListParser;
import com.dssm.esc.model.jsonparser.emergency.GetEmergencyGropDataParser;
import com.dssm.esc.model.jsonparser.emergency.GetEventValuationParser;
import com.dssm.esc.model.jsonparser.emergency.GetNotiConfigContentParser;
import com.dssm.esc.model.jsonparser.emergency.GetPerformPlanParser;
import com.dssm.esc.model.jsonparser.emergency.GetPlanDetailParser;
import com.dssm.esc.model.jsonparser.emergency.GetPrecautionByPlanResParser;
import com.dssm.esc.model.jsonparser.emergency.GetSignEmergencyInfoParser;
import com.dssm.esc.model.jsonparser.emergency.PlanAuthParser;
import com.dssm.esc.model.jsonparser.emergency.PlanNameParser;
import com.dssm.esc.model.jsonparser.emergency.PlanProcessListParser;
import com.dssm.esc.model.jsonparser.emergency.PlanStarBohuiParser;
import com.dssm.esc.model.jsonparser.emergency.PlanStarEventListParser;
import com.dssm.esc.model.jsonparser.emergency.PlanStarListDetailParser;
import com.dssm.esc.model.jsonparser.emergency.PlanStarParser;
import com.dssm.esc.model.jsonparser.emergency.PlanStarPlanListParser;
import com.dssm.esc.model.jsonparser.emergency.PlanSuspandParser;
import com.dssm.esc.model.jsonparser.emergency.ReValuationEventParser;
import com.dssm.esc.model.jsonparser.emergency.SendNoticeParser;
import com.dssm.esc.model.jsonparser.emergency.SignInParser;
import com.dssm.esc.model.jsonparser.emergency.SuspandParser;
import com.dssm.esc.model.jsonparser.emergency.SwitchOverParser;
import com.dssm.esc.util.Const;

import java.util.List;
import java.util.Map;


public class EmergencyServiceImpl implements EmergencyService {
	private static EmergencyServiceImpl emergencyServiceImpl = null;

	/**
	 * 获取EmergencyServiceImpl对象
	 * 
	 * @return
	 */
	public synchronized static EmergencyServiceImpl getEmergencySeviceImpl() {
		// TODO Auto-generated method stub
		if (emergencyServiceImpl == null) {
			emergencyServiceImpl = new EmergencyServiceImpl();
		}
		return emergencyServiceImpl;
	}

	/**
	 * 不允许直接访问构造方法
	 */
	private EmergencyServiceImpl() {

	}

	/**
	 * 设置Boolean值返回数据
	 * 
	 * @param listenser
	 * @param object
	 * @param error
	 */
	private void setEmergencyBooleanListenser(
			final EmergencySeviceImplBackBooleanListenser listenser,
			Object object, String error) {
		boolean flag = false;
		String stRerror = null;
		String Exceptionerror = null;
		if (object != null) {
			Map<String, String> map = (Map<String, String>) object;
			if (map.containsKey("success") && map.containsKey("message")) {
				if (map.get("success").equals("true")) {
					flag = true;
				} else {
					flag = false;
					stRerror = "失败";
				}
				stRerror = map.get("message");
			} else {
				stRerror = "未访问到数据";
			}
		} else {
			Exceptionerror = error;
		}

		listenser.setEmergencySeviceImplListenser(flag, stRerror,
				Exceptionerror);
	}

	/**
	 * 设置集合返回数据
	 * 
	 * @param listenser
	 * @param object
	 * @param error
	 */
	private void setEmergencyListListenser(
			final EmergencySeviceImplListListenser listenser, Object object,
			String error) {
		List<Object> list = null;
		String Exceptionerror = null;
		if (object != null) {
			list = (List<Object>) object;

		} else {
			Exceptionerror = error;
		}
		listenser.setEmergencySeviceImplListListenser(list, null,
				Exceptionerror);
	}

	public interface EmergencySeviceImplBackValueListenser {
		/**
		 * 当backValue！=null时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时backValue，Exceptionerror都为null；
		 * 当Exceptionerror！=null时backValue，stRerror都为null； backValue回调得到值
		 * stRerror回调得到接口返回错误信息 Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setEmergencySeviceImplListenser(String backValue, String stRerror,
											 String Exceptionerror);
	}

	public interface EmergencySeviceImplBackBooleanListenser {
		/**
		 * 当backflag=true,false时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时backflag=false，Exceptionerror都为null；
		 * 当Exceptionerror！=null时backflag=false，stRerror都为null；
		 * backflag回调得到状态true，false， stRerror回调得到接口返回错误信息
		 * Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setEmergencySeviceImplListenser(Boolean backflag, String stRerror,
											 String Exceptionerror);
	}

	public interface EmergencySeviceImplListListenser {
		/**
		 * 当object=!null时stRerror，Exceptionerror都为null；
		 * 当stRerror！=null时object，Exceptionerror都为null；
		 * 当Exceptionerror！=null时object，stRerror都为null；
		 * 
		 * stRerror回调得到接口返回错误信息 Exceptionerror回调得到接口返回异常信息
		 * 
		 * @param object
		 * @param stRerror
		 * @param Exceptionerror
		 */
		void setEmergencySeviceImplListListenser(Object object,
												 String stRerror, String Exceptionerror);
	}

	/**
	 * 获取业务类型和事件等级
	 */
	@Override
	public void getBusinessType(int tag,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new BusinessTypeParser(tag, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 获取事件场景
	 */
	@Override
	public void getEventScene(final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new EventSceneParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 获取预案名称
	 */
	@Override
	public void getPlanName(final int tags, String id,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new PlanNameParser(tags, id, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				if (tags == 2) {

					PlanNameSelectEntity planNameSelectEntity = null;
					String stRerror = null;
					String Exceptionerror = null;
					if (object != null) {
						planNameSelectEntity = (PlanNameSelectEntity) object;
					} else if (error != null) {
						Exceptionerror = error;
					} else {
						stRerror = Const.NO_MSG;
					}

					listenser.setEmergencySeviceImplListListenser(
							planNameSelectEntity, stRerror, Exceptionerror);
				} else if (tags == 1 || tags == 3) {
					setEmergencyListListenser(listenser, object, error);
				}
			}
		});

	}

	/**
	 * 获取演练计划表
	 */
	@Override
	public void getDrillProjectName(
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new DrillProjectNameParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 获取演练详情
	 */
	@Override
	public void getDrillProjectNameDetail(String detailPlanId,
			String drillPlanName,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (detailPlanId == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}

		new DrillPrecautionDetailParser(detailPlanId, drillPlanName,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						DrillProcationDetailEntity detailEntity = null;
						String stRerror = null;
						String Exceptionerror = null;
						if (object != null) {
							detailEntity = (DrillProcationDetailEntity) object;
						} else if (error != null) {
							Exceptionerror = error;
						} else {
							stRerror = Const.NO_MSG;
						}

						listenser.setEmergencySeviceImplListListenser(
								detailEntity, stRerror, Exceptionerror);
					}
				});
	}

	/**
	 * 添加评估
	 * 
	 * @param tag
	 *            1,应急;2,演练
	 * @param addEntity
	 * @param listenser
	 */
	@Override
	public void addEmergencyPlanevent(String tag,
			EmergencyPlanEvaAddEntity addEntity,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		new EmergencyPlanEvaAddParser(tag, addEntity,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});
	}

	/**
	 * 获取事件未启动列表
	 */
	@Override
	public void getPlanStarList(final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new PlanStarEventListParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});
	}

	/**
	 * 获取启动事件详情
	 * 
	 * @param id
	 * @param listenser
	 */
	@Override
	public void getPlanStarListDetail(final String id,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub

		if (id == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new PlanStarListDetailParser(id, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				PlanStarListDetailEntity detailEntity = null;
				String stRerror = null;
				String Exceptionerror = null;
				if (object != null) {
					detailEntity = (PlanStarListDetailEntity) object;
				} else if (error != null) {
					Exceptionerror = error;
				} else {
					stRerror = Const.NO_MSG;
				}

				listenser.setEmergencySeviceImplListListenser(detailEntity,
						stRerror, Exceptionerror);
			}
		});

	}

	/**
	 * 预案驳回
	 */
	@Override
	public void planStarBohui(String planEveId, String planEveName,
			String submitterId, String eveType,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		new PlanStarBohuiParser(planEveId, planEveName, submitterId, eveType,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});
	}

	/**
	 * 预案启动
	 */
	@Override
	public void planStar(String id, String usePlan,
			PlanStarListDetailObjEntity detailObjEntity,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new PlanStarParser(id, usePlan, detailObjEntity,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});
	}

	/**
	 * 获取驳回事件列表
	 */
	@Override
	public void getBoHuiList(final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new BoHuiEventListParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});
	}

	/**
	 * 获取事件评估信息
	 */
	@Override
	public void getEventInfo(String id,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetEventValuationParser(id, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				GetProjectEveInfoEntity entity = null;
				String stRerror = null;
				String Exceptionerror = null;
				if (object != null) {
					entity = (GetProjectEveInfoEntity) object;
				} else if (error != null) {
					Exceptionerror = error;
				} else {
					stRerror = Const.NO_MSG;
				}

				listenser.setEmergencySeviceImplListListenser(entity, stRerror,
						Exceptionerror);

			}
		});
	}

	/**
	 * 重新评估
	 */
	@Override
	public void reValuationEvent(GetProjectEveInfoEntity entity,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (entity == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new ReValuationEventParser(entity, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 事件删除
	 */
	@Override
	public void deleteEvent(String id,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new DeleteEventParser(id, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});
	}

	/**
	 * 预案详情
	 */
	@Override
	public void getPlanDetail(String id,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetPlanDetailParser(id, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				PlanDetailEntity entity = null;
				String stRerror = null;
				String Exceptionerror = null;
				if (object != null) {
					entity = (PlanDetailEntity) object;
				} else if (error != null) {
					Exceptionerror = error;
				} else {
					stRerror = Const.NO_MSG;
				}

				listenser.setEmergencySeviceImplListListenser(entity, stRerror,
						Exceptionerror);

			}
		});

	}

	/**
	 * 预案中止
	 */
	@Override
	public void planSuspand(PlanSuspandEntity suspandEntity,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub

		new PlanSuspandParser(suspandEntity, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 预案授权
	 */
	@Override
	public void planAuth(String id, String planAuthOpition, String planName,
			String planResName, String planResType, String planId,
			String planStarterId, String submitterId,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new PlanAuthParser(id, planAuthOpition, planName, planResName,
				planResType, planId, planStarterId, submitterId,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});

	}

	/**
	 * 签到详情
	 */
	@Override
	public void getSignEmergencyInfo(String planInfoId,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (planInfoId == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetSignEmergencyInfoParser(planInfoId,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyListListenser(listenser, object, error);
					}
				});

	}

	/**
	 * 判断用户是否签到
	 */
	@Override
	public void checkEmergencySign(String planInfoId,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (planInfoId == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new CheckEmergencySignParser(planInfoId, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 签到
	 */
	@Override
	public void signIn(String planInfoId,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (planInfoId == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new SignInParser(planInfoId, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 预案步骤
	 */
	@Override
	public void getPlanProcessList(String planInfoId,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (planInfoId == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new PlanProcessListParser(planInfoId, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 人员指派
	 */
	@Override
	public void assign(String id, String planInfoId, String executePeopleId,
			String executePeople,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new AssignParser(id, planInfoId, executePeopleId, executePeople,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});
	}

	/**
	 * 获取执行列表
	 */
	@Override
	public void getPlanExecute(final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new GetPrecautionByPlanResParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});
	}

	/**
	 * 任务执行
	 */
	@Override
	public void getBeginPlan(String id, String planInfoId,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new BeginExecutePlanParser(id, planInfoId,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});

	}

	/**
	 * 任务切换
	 */
	@Override
	public void swichOver(String id, String planInfoId, String status,
			String message,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		if (id == null) {
			listenser.setEmergencySeviceImplListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new SwitchOverParser(id, planInfoId, status, message,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyBooleanListenser(listenser, object, error);
					}
				});

	}

	/**
	 * 待执行步骤
	 */
	@Override
	public void getPerformPlanExcute(String planInfoId,
			final EmergencySeviceImplListListenser listListenser) {
		// TODO Auto-generated method stub
		if (planInfoId == null) {
			listListenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetPerformPlanParser(planInfoId, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listListenser, object, error);
			}
		});

	}

	/**
	 * 发送通告
	 */
	@Override
	public void sendNotice(SendNoticyEntity noticyEntity,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		new SendNoticeParser(noticyEntity, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});
	}

	/**
	 * 列表展示 a:1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示
	 */
	@Override
	public void getAuthlist(int a,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub

		new GetAuthPlanListParser(a, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});
	}

	/**
	 * 预案小组列表
	 * 
	 * @param planInfoId
	 *            预案执行id
	 * @param precautionId
	 *            预案id
	 */
	@Override
	public void getEmergencyGropData(String planInfoId, String precautionId,
			final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (planInfoId == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetEmergencyGropDataParser(planInfoId, precautionId,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						setEmergencyListListenser(listenser, object, error);
					}
				});

	}

	/**
	 * 根据对象和阶段获取对应的配置内容
	 */
	@Override
	public void getNotiConfigContent(String precautionId, String type,
			String stage, final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		if (precautionId == null) {
			listenser.setEmergencySeviceImplListListenser(null,
					Const.PARAMETER_NULL, null);
			return;
		}
		new GetNotiConfigContentParser(precautionId, type, stage,
				new OnDataCompleterListener() {

					@Override
					public void onEmergencyParserComplete(Object object,
							String error) {
						// TODO Auto-generated method stub
						if (object != null) {
							GetProjectEveInfoEntity entity = null;
							String stRerror = null;
							String Exceptionerror = null;
							if (object != null) {
								entity = (GetProjectEveInfoEntity) object;
							} else if (error != null) {
								Exceptionerror = error;
							} else {
								stRerror = Const.NO_MSG;
							}

							listenser.setEmergencySeviceImplListListenser(
									entity, stRerror, Exceptionerror);
						}
					}
				});

	}

	/**
	 * 获取已启动预案列表
	 */
	@Override
	public void getStarList(final EmergencySeviceImplListListenser listenser) {
		// TODO Auto-generated method stub
		new PlanStarPlanListParser(new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyListListenser(listenser, object, error);
			}
		});

	}

	/**
	 * 4.2.8预案中止（+）
	 */
	@Override
	public void suspand(PlanSuspandEntity entity,
			final EmergencySeviceImplBackBooleanListenser listenser) {
		// TODO Auto-generated method stub
		new SuspandParser(entity, new OnDataCompleterListener() {

			@Override
			public void onEmergencyParserComplete(Object object, String error) {
				// TODO Auto-generated method stub
				setEmergencyBooleanListenser(listenser, object, error);
			}
		});
	}

}
